package maxi.med.auth.repository;

import java.util.Optional;
import maxi.med.auth.entity.BannedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BannedTokenRepository extends JpaRepository<BannedToken, String> {
    Optional<BannedToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO banned_tokens (token) VALUES (:#{#bannedToken.token}) ON CONFLICT (token) DO NOTHING",
            nativeQuery = true)
    void insertIfNotExist(@Param("bannedToken") BannedToken bannedToken);
}
