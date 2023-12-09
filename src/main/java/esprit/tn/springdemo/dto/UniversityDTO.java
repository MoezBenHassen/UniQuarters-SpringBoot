package esprit.tn.springdemo.dto;

import esprit.tn.springdemo.entities.Foyer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversityDTO {
    private Long id;
    private String nom;
    private String adresse;
    private MultipartFile image;
    private Foyer foyer;

}
