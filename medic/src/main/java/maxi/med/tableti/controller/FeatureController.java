package maxi.med.tableti.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.tableti.dto.AddFeatureDto;
import maxi.med.tableti.dto.UserFeaturesDto;
import maxi.med.tableti.service.FeatureService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://192.168.0.101:5173", "http://localhost:5173", "http://89.169.169.19:5173"})
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping
    public UserFeaturesDto getUserFeatures(@RequestHeader("userId") long userId) {
        return featureService.getUserFeatures(userId);
    }

    @PostMapping
    public UserFeaturesDto createUserFeatures(@RequestHeader("userId") long userId, @RequestBody AddFeatureDto dto) {
        return featureService.addUserFeature(userId, dto);
    }

    @GetMapping("/all")
    public List<String> getFeaturesTitles() {
        log.info("spisok");
        return featureService.getFeaturesTitles();
    }
}
