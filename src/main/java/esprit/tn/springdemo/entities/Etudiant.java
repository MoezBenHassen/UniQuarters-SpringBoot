package esprit.tn.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String nom;
    String prenom;
    long cin;
    String ecole;
    Date dateNaissance;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Reservation> reservations;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;



    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", cin=" + cin +
                ", ecole='" + ecole + '\'' +
                ", dateNaissance=" + dateNaissance +
                '}';
    }
}
