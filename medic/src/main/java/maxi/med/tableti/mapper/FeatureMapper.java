package maxi.med.tableti.mapper;

import java.util.List;
import maxi.med.tableti.dto.AddFeatureDto;
import maxi.med.tableti.dto.UserFeaturesDto;
import maxi.med.tableti.entity.Feature;
import maxi.med.tableti.entity.UserFeature;

public class FeatureMapper {
    public static UserFeaturesDto mapUserFeaturesToUserFeaturesDto(List<UserFeature> userFeatures) {
        if (userFeatures.isEmpty()) {
            return new UserFeaturesDto(List.of());
        }
        return new UserFeaturesDto(userFeatures.stream()
                .map(userFeature -> userFeature.feature().title())
                .toList());
    }

    public static Feature mapAddFeatureDtoToFeature(AddFeatureDto addFeatureDto) {
        Feature feature = new Feature();
        feature.title(addFeatureDto.title());
        return feature;
    }
}
