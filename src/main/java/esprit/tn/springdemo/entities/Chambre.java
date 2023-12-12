package esprit.tn.springdemo.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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

    private boolean isAvailable;
    @Transient
    private double review;


    private boolean wifi;
    private boolean airConditioning;
    private boolean privateBathroom;
    private boolean balcony;
    private boolean workspace;
    private boolean kitchenette;
    private boolean petFriendly;

     private boolean travaux;

    // Constructors, getters, and setters
    public boolean calculateAvailability() {
        if (reservations == null) {
            return true;
        }
        int numberOfReservations = reservations.size();
        return numberOfReservations < capacity;
    }

    public void updateAvailability() {
        this.isAvailable = calculateAvailability();
        calculateReview();

    }

     public double calculateReview() {
        int totalFeatures = 7; 
        int trueFeatureCount = 0;

        if (wifi) trueFeatureCount++;
        if (airConditioning) trueFeatureCount++;
        if (privateBathroom) trueFeatureCount++;
        if (balcony) trueFeatureCount++;
        if (workspace) trueFeatureCount++;
        if (kitchenette) trueFeatureCount++;
        if (petFriendly) trueFeatureCount++;

        this.review = (double) trueFeatureCount / totalFeatures * 5.0;
        return this.review;
    }
    @Override
    public String toString() {
        return "Chambre{" +
                "id=" + id +
                ", numero=" + numero +
                ", capacity=" + capacity +
                ", isAvailable=" + isAvailable +
                ", review=" + review +
                ", description='" + description + '\'' +
                ", chambreType=" + type +
                ", reservations=" + reservations +
                ", wifi=" + wifi +
                ", airConditioning=" + airConditioning +
                ", privateBathroom=" + privateBathroom +
                ", balcony=" + balcony +
                ", workspace=" + workspace +
                ", kitchenette=" + kitchenette +
                ", petFriendly=" + petFriendly +
                ", travaux=" + travaux +
                '}';
    }
}