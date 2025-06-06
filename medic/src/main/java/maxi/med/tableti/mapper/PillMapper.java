package maxi.med.tableti.mapper;

import lombok.experimental.UtilityClass;
import maxi.med.tableti.dto.PillDto;
import maxi.med.tableti.entity.Tablet;
import maxi.med.tableti.entity.UserTablet;

@UtilityClass
public class PillMapper {

    public static PillDto mapTabletToPillDto(Tablet tablet) {
        return new PillDto(tablet.title(), tablet.description(), tablet.imageUrl());
    }

    public static PillDto mapUserTabletToPillDto(UserTablet userTablet) {
        return new PillDto(
                userTablet.tablet().title(),
                userTablet.tablet().description(),
                userTablet.tablet().imageUrl());
    }
}
