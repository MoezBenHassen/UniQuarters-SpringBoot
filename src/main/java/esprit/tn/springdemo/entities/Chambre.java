package esprit.tn.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long numero;
    private long capacity;
    private String description;


    @Enumerated(EnumType.STRING)
    private TypeChambre type;



    /*@JsonIgnore*/
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Bloc bloc;

    @OneToMany
    private Set<Reservation> reservations;


    // Constructors, getters, and setters
    @Transient
    private boolean isAvailable;
    public boolean calculateAvailability() {
        int numberOfReservations = reservations.size();
        return numberOfReservations < capacity;
    }

    public void updateAvailability() {
        this.isAvailable = calculateAvailability();
    }
    @Override
    public String toString() {
        return "Chambre{" +
                "id=" + id +
                ", numero=" + numero +
                ", capacity=" + capacity +
                ", isAvailable=" + isAvailable +
                ", description='" + description + '\'' +
                ", chambreType=" + type +
                ", bloc=" + bloc +
                ", reservations=" + reservations +
                '}';
    }
}