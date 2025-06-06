package maxi.med.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "banned_tokens")
@Setter
@Getter
public class BannedToken {
    @Id
    private String token;
}
