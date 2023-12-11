package esprit.tn.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Foyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private long capacite;
    private double lng;
    private double lat;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Universite universite;


    @OneToMany(mappedBy = "foyer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Bloc> blocs;

    @Override
    public String toString() {
        return "Foyer{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", capacite=" + capacite +
                ", universite=" + universite +
                '}';
    }
}
