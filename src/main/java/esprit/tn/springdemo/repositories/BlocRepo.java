package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlocRepo extends JpaRepository<Bloc, Long> {
    public Bloc findByNom(String nom);

   Bloc findBlocByChambres(Chambre chambre);
}
