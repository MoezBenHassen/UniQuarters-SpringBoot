package esprit.tn.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bloc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String text;
    private String color;
    private String capacite;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JsonBackReference
    private Foyer foyer;
  
    @OneToMany(cascade= CascadeType.ALL , mappedBy = "bloc")
    @JsonIgnore
    private List<Chambre> chambres;

    @Override
    public String toString() {
        return "Bloc{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", capacite='" + capacite + '\'' +
                ", foyer=" + foyer +
                ", chambres=" + chambres +
                '}';
    }

}