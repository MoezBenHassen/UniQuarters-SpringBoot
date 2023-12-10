package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Foyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FoyerRepo extends JpaRepository<Foyer, Long> {
Foyer findFoyerByNom(String nom);

}
