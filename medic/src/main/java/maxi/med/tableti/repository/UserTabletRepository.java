package maxi.med.tableti.repository;

import java.util.List;
import maxi.med.tableti.entity.Tablet;
import maxi.med.tableti.entity.User;
import maxi.med.tableti.entity.UserTablet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTabletRepository extends JpaRepository<UserTablet, Long> {
    List<UserTablet> findByUser(User user);

    UserTablet findByUserAndTablet(User user, Tablet tablet);
}
