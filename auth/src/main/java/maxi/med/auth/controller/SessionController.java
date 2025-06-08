package maxi.med.auth.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.auth.dto.request.LoginByEmailBody;
import maxi.med.auth.dto.response.CurrentSessionResponse;
import maxi.med.auth.dto.response.SessionResponse;
import maxi.med.auth.dto.response.SessionValidationResponse;
import maxi.med.auth.service.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/sessions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(
        origins = {
            "http://89.169.169.19:5173",
            "http://192.168.0.101:5173",
            "http://192.168.0.107:5173",
            "http://localhost:5173"
        })
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/login")
    public SessionResponse createSession(@RequestBody LoginByEmailBody body) {
        log.info("auth");
        return sessionService.createSessionByEmail(body);
    }

    @GetMapping("/isactive/{active}")
    public List<CurrentSessionResponse> getCurrentSession(Authentication authentication, @PathVariable Boolean active) {
        return sessionService.getAllSessionsByActive(authentication, active);
    }

    @PatchMapping("/logout")
    public SessionResponse logout(Authentication authentication) {
        log.info(authentication.getCredentials().toString());
        return sessionService.logout(authentication);
    }

    @GetMapping("/validate")
    public SessionValidationResponse validateSession(@RequestHeader("Authorization") String authHeader) {
        return sessionService.validateSession(authHeader);
    }
}
