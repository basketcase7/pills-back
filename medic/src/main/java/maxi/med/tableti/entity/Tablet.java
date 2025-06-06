package maxi.med.tableti.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tablet", schema = "medic")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tablet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "tablet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TabletFeature> tabletFeatures = new HashSet<>();

    @OneToMany(mappedBy = "tablet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserTablet> userTablets = new HashSet<>();
}
