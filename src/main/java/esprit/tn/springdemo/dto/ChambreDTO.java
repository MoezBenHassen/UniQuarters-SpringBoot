package esprit.tn.springdemo.dto;
import esprit.tn.springdemo.entities.TypeChambre;

public class ChambreDTO {
    private Long id;
    private long numero;
    private long capacity;
    private String description;
    private TypeChambre type;
    private String blocNom;
    private String foyerNom;
    private String universiteNom;

    // Constructors, getters, and setters

    @Override
    public String toString() {
        return "ChambreWithDetailsDTO{" +
                "id=" + id +
                ", numero=" + numero +
                ", capacity=" + capacity +
                ", description='" + description + '\'' +
                ", chambreType=" + type +
                ", blocNom='" + blocNom + '\'' +
                ", foyerNom='" + foyerNom + '\'' +
                ", universiteNom='" + universiteNom + '\'' +
                '}';
    }
}
