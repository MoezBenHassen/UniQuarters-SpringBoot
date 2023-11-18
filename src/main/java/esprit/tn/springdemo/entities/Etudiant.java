package esprit.tn.springdemo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
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

    @ManyToMany(mappedBy = "etudiants")
    //@ManyToMany
    private List<Reservation> reservations;

    public void setIdEtudiant(long idEtudiant) {
        this.id = idEtudiant;
    }
}
