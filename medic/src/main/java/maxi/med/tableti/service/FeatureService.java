package maxi.med.tableti.service;

import java.util.List;
import maxi.med.tableti.dto.AddFeatureDto;
import maxi.med.tableti.dto.UserFeaturesDto;

public interface FeatureService {
    UserFeaturesDto getUserFeatures(Long userId);

    UserFeaturesDto addUserFeature(Long userId, AddFeatureDto addFeatureDto);

    List<String> getFeaturesTitles();
}
