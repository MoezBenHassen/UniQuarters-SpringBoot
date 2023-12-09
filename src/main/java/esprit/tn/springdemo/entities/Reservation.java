package esprit.tn.springdemo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private LocalDate anneeUniversitaire;
    private Boolean estValide;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    @ManyToMany
    private List<Etudiant> etudiants;
}
