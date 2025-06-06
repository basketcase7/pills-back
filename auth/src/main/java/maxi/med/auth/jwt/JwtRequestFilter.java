package maxi.med.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.auth.entity.BannedToken;
import maxi.med.auth.entity.Session;
import maxi.med.auth.entity.User;
import maxi.med.auth.repository.BannedTokenRepository;
import maxi.med.auth.repository.SessionRepository;
import maxi.med.auth.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final BannedTokenRepository bannedTokenRepository;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            boolean isBanned = bannedTokenRepository.findByToken(jwt).isPresent();
            if (isBanned) {
                log.info("Attempt to use a banned token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is banned");
                return;
            }
            try {
                Long userId = jwtTokenUtils.getUserIdFromToken(jwt);
                if (userId != null) {
                    User user = userRepository.findById(userId).orElse(null);

                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken(user, jwt, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (ExpiredJwtException e) {
                log.debug("Token is expired :(");

                Session session = sessionRepository.findByToken(jwt).orElse(null);
                BannedToken bannedToken = new BannedToken();

                bannedToken.token(jwt);
                try {
                    bannedTokenRepository.insertIfNotExist(bannedToken);

                    if (session != null) {
                        if (session.active()) {
                            session.active(false);
                            sessionRepository.save(session);
                        }
                    }

                } catch (Exception ex) {
                    return;
                }
            } catch (Exception e) {
                log.error("Error processing JWT", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
