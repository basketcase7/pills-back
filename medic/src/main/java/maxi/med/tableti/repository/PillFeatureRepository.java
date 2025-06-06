package maxi.med.tableti.repository;

import java.util.List;
import maxi.med.tableti.entity.Tablet;
import maxi.med.tableti.entity.TabletFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PillFeatureRepository extends JpaRepository<TabletFeature, Long> {
    List<TabletFeature> findAllByTablet(Tablet tablet);
}
