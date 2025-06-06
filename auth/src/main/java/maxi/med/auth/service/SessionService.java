package maxi.med.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.auth.dto.request.LoginByEmailBody;
import maxi.med.auth.dto.response.CurrentSessionResponse;
import maxi.med.auth.dto.response.SessionResponse;
import maxi.med.auth.dto.response.SessionValidationResponse;
import maxi.med.auth.entity.BannedToken;
import maxi.med.auth.entity.Session;
import maxi.med.auth.entity.User;
import maxi.med.auth.exception.AccessRightsException;
import maxi.med.auth.exception.SessionNotFoundException;
import maxi.med.auth.jwt.JwtTokenUtils;
import maxi.med.auth.mapper.SessionMapper;
import maxi.med.auth.repository.BannedTokenRepository;
import maxi.med.auth.repository.SessionRepository;
import maxi.med.auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final SessionRepository sessionRepository;
    private final BannedTokenRepository bannedTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public SessionResponse createSessionByEmail(LoginByEmailBody body) {
        User user = userRepository
                .findByEmail(body.email())
                .filter(u -> passwordEncoder.matches(body.password(), u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect email or password"));

        user.sessions().forEach(session -> {
            session.active(false);
            sessionRepository.save(session);
        });

        String token = jwtTokenUtils.generateToken(user);
        Date expiredDate = jwtTokenUtils.getExpirationDateFromToken(token);

        Session session = new Session();
        session.user(user);
        session.token(token);
        session.expirationTime(expiredDate);
        session.active(true);

        session = sessionRepository.save(session);

        return SessionMapper.mapSessionResponse(session);
    }

    public List<CurrentSessionResponse> getAllSessions(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        List<Session> sessions = sessionRepository.findByUserId(user.id());
        Date now = new Date();
        return sessions.stream()
                .map(session -> {
                    boolean isActive = session.expirationTime().after(now);
                    return SessionMapper.mapCurrentSessionResponse(session, isActive);
                })
                .toList();
    }

    public List<CurrentSessionResponse> getAllSessionsByActive(Authentication authentication, Boolean active) {
        User user = userService.getUserByAuthentication(authentication);

        List<Session> sessions = sessionRepository.findByActive(active);
        return sessions.stream()
                .map(session -> {
                    return SessionMapper.mapCurrentSessionResponse(session, active);
                })
                .toList();
    }

    public CurrentSessionResponse getCurrentSession(Authentication authentication) {
        String currentToken = (String) authentication.getCredentials();

        Optional<Session> sessionOptional = sessionRepository.findByToken(currentToken);
        if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            Date now = new Date();
            return SessionMapper.mapCurrentSessionResponse(
                    session,
                    jwtTokenUtils.getExpirationDateFromToken(currentToken).after(now));
        } else {
            throw new SessionNotFoundException("Session not found");
        }
    }

    public ResponseEntity<?> deleteSessionById(Authentication authentication, String id) {
        Long currentUserId = userService.getUserByAuthentication(authentication).id();
        Long sessionId = Long.valueOf(id);

        Session session = sessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if (!session.user().id().equals(currentUserId)) {
            throw new AccessRightsException("You can only delete your own sessions");
        }

        BannedToken bannedToken = new BannedToken();
        bannedToken.token(authentication.getCredentials().toString());

        bannedTokenRepository.save(bannedToken);
        sessionRepository.deleteById(sessionId);

        return ResponseEntity.ok().build();
    }

    public SessionResponse logout(Authentication authentication) {
        String currentToken = (String) authentication.getCredentials();
        Session session = sessionRepository
                .findByToken(currentToken)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        BannedToken bannedToken = new BannedToken();
        bannedToken.token(currentToken);
        bannedTokenRepository.save(bannedToken);

        session.active(false);
        sessionRepository.save(session);

        return SessionMapper.mapSessionResponse(session);
    }

    public SessionValidationResponse validateSession(String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        try {
            if (!validateToken(token)) {
                return new SessionValidationResponse(null, false);
            }

            Long userId = jwtTokenUtils.getUserIdFromToken(token);

            return new SessionValidationResponse(userId, true);

        } catch (ExpiredJwtException e) {
            return new SessionValidationResponse(null, false);
        } catch (JwtException | IllegalArgumentException e) {
            return new SessionValidationResponse(null, false);
        }
    }

    private boolean validateToken(String token) {
        try {
            log.info("{}", jwtTokenUtils.secret());
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtTokenUtils.secret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}
