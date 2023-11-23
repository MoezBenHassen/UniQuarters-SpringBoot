package esprit.tn.springdemo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    private String id;

    private LocalDate anneeUniversitaire;
    private Boolean estValide;

    @ManyToMany(mappedBy = "reservations")
    private Set<Etudiant> etudiants;

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", anneeUniversitaire=" + anneeUniversitaire +
                ", estValide=" + estValide +
                '}';
    }
}
