package maxi.med.tableti.repository;

import java.util.List;
import java.util.Optional;
import maxi.med.tableti.entity.Feature;
import maxi.med.tableti.entity.User;
import maxi.med.tableti.entity.UserFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeatureRepository extends JpaRepository<UserFeature, Long> {
    List<UserFeature> findAllByUser(User user);

    Optional<UserFeature> findByUserAndFeature(User user, Feature feature);
}
