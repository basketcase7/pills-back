package maxi.med.tableti.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.tableti.dto.AddFeatureDto;
import maxi.med.tableti.dto.UserFeaturesDto;
import maxi.med.tableti.entity.Feature;
import maxi.med.tableti.entity.User;
import maxi.med.tableti.entity.UserFeature;
import maxi.med.tableti.exception.EntityNotFoundException;
import maxi.med.tableti.mapper.FeatureMapper;
import maxi.med.tableti.repository.FeatureRepository;
import maxi.med.tableti.repository.UserFeatureRepository;
import maxi.med.tableti.repository.UserRepository;
import maxi.med.tableti.service.FeatureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final UserFeatureRepository userFeatureRepository;
    private final UserRepository userRepository;
    private final FeatureRepository featureRepository;

    @Override
    @Transactional
    public UserFeaturesDto getUserFeatures(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return FeatureMapper.mapUserFeaturesToUserFeaturesDto(userFeatureRepository.findAllByUser(user));
    }

    @Override
    @Transactional
    public UserFeaturesDto addUserFeature(Long userId, AddFeatureDto addFeatureDto) {
        log.info(addFeatureDto.title());
        Feature feature = featureRepository
                .findByTitle(addFeatureDto.title())
                .orElseThrow(() -> new EntityNotFoundException("Feature not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserFeature userFeature = new UserFeature();
        userFeature.feature(feature);
        userFeature.user(user);

        Optional<UserFeature> newUserFeature = userFeatureRepository.findByUserAndFeature(user, feature);
        if (newUserFeature.isEmpty()) {
            userFeatureRepository.save(userFeature);
        }

        return FeatureMapper.mapUserFeaturesToUserFeaturesDto(userFeatureRepository.findAllByUser(user));
    }

    @Override
    public List<String> getFeaturesTitles() {
        return featureRepository.findAll().stream().map(Feature::title).toList();
    }
}
