package maxi.med.auth.repository;

import java.util.List;
import java.util.Optional;
import maxi.med.auth.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUserId(Long userId);

    List<Session> findByActive(boolean active);

    Optional<Session> findByToken(String token);
}
