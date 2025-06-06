package maxi.med.tableti.repository;

import java.util.Optional;
import maxi.med.tableti.entity.Tablet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PillsRepository extends JpaRepository<Tablet, Long> {
    Optional<Tablet> findByTitle(String title);
}
