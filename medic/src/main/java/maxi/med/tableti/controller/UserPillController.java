package maxi.med.tableti.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.tableti.dto.PillDto;
import maxi.med.tableti.service.PillsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user_pill")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {"http://192.168.0.101:5173", "http://localhost:5173", "http://89.169.169.19:5173"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true")
@Slf4j
public class UserPillController {

    private final PillsService pillsService;

    @GetMapping
    public List<PillDto> getUserPills(@RequestHeader("userId") Long userId) {
        log.info("da");
        return pillsService.getUserPills(userId);
    }

    @PostMapping
    public PillDto addUserPill(HttpServletRequest request, @RequestParam String pillTitle) {
        Long userId = (Long) request.getAttribute("userId");
        return pillsService.addUserPill(userId, pillTitle);
    }
}
