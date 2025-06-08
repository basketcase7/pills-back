package maxi.med.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.auth.dto.request.RegisterBody;
import maxi.med.auth.dto.response.SessionResponse;
import maxi.med.auth.dto.response.UserResponse;
import maxi.med.auth.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(
        origins = {
            "http://89.169.169.19:5173",
            "http://192.168.0.101:5173",
            "http://192.168.0.107:5173",
            "http://localhost:5173"
        })
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public SessionResponse registerUser(@RequestBody @Valid RegisterBody body) {
        return userService.registerUser(body);
    }

    @GetMapping("/profile")
    public UserResponse getUser(Authentication authentication) {
        return userService.getUserResponseByAuthentication(authentication);
    }

    @GetMapping("/user/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        Long uuid = Long.valueOf(id);

        return userService.findUserById(uuid);
    }
}
