package maxi.med.tableti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "user_tablet",
        schema = "medic",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "tablet_id"}))
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserTablet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ut_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tablet_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ut_tablet"))
    private Tablet tablet;
}
