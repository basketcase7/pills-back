package maxi.med.tableti.repository;

import java.util.Optional;
import maxi.med.tableti.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findByTitle(String title);
}
