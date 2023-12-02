package esprit.tn.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.serializer.Serializer;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Universite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String adresse;
    private String image;

    @OneToOne(mappedBy = "universite",cascade = CascadeType.ALL)
    private Foyer foyer;

    @Override
    public String toString() {
        if (foyer == null) {
            return "Universite{" +
                    "id=" + id +
                    ", nom='" + nom + '\'' +
                    ", adresse='" + adresse + '\'' +
                    '}';
        } else {
            return "Universite{" +
                    "id=" + id +
                    ", nom='" + nom + '\'' +
                    ", adresse='" + adresse + '\'' +
                    ", foyer='" + foyer.getId() + '\'' +
                    '}';
        }

    }
}
