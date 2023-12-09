package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Foyer;
import esprit.tn.springdemo.entities.Universite;
import esprit.tn.springdemo.repositories.FoyerRepo;
import esprit.tn.springdemo.repositories.UniversiteRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UniversiteServiceImpl implements IUniversiteService {
    private final UniversiteRepo universiteRepo;
    private final FoyerRepo foyerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Universite> retrieveAllUniversities() {
        return universiteRepo.findAll();
    }

    @Override
    public Universite addUniversity(Universite u) {

        u.getFoyer().setUniversite(u);
        foyerRepo.save(u.getFoyer());
        return universiteRepo.save(u);
    }

    @Override
    public Universite updateUniversity(Universite u,Long id) {

        Universite uni =universiteRepo.getById(id);
        Foyer f= uni.getFoyer();
        uni.setAdresse(u.getAdresse());
        f.setLat(u.getFoyer().getLat());
        f.setLng(u.getFoyer().getLng());
        uni.setNom(u.getNom());
        if(u.getImage()!=null){
            uni.setImage(u.getImage());
        }

        f.setNom(u.getFoyer().getNom());
        f.setCapacite(u.getFoyer().getCapacite());
        foyerRepo.save(f);
        return universiteRepo.save(uni);
    }

    @Override
    public Universite retrieveUniversity(long idUniversity) {
        return universiteRepo.findById(idUniversity).orElse(null);
    }

    @Override
    public Universite affectFoyer(long idUniversity, long idFoyer) {
        Universite universite = universiteRepo.findById(idUniversity).orElse(null);
        System.out.println("founded uni" + universite);
        if (universite == null) {
            throw new RuntimeException("Univesite not found");
        }
        Foyer foyer = foyerRepo.findById(idFoyer).orElse(null);
        System.out.println("founded foyer: " + foyer);
        if (foyer == null) {
            throw new RuntimeException("Foyer not found");
        }
        foyer.setUniversite(universite);
        Foyer updatedFoyer = foyerRepo.save(foyer);
        entityManager.clear(); // Clearing the Hibernate session ensures that subsequent entity queries fetch the latest data from the database.
        System.out.println("updatedFoyer = " + updatedFoyer);
        Universite updatedUniversite = universiteRepo.findById(idUniversity).orElse(null);
        System.out.println("updatedUniversite = " + updatedUniversite);
        return updatedUniversite;
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        System.out.println("Starting desafffecting");
        //Universite universite = retrieveUniversity(idUniversite);
        Universite universite = universiteRepo.findById(idUniversite).orElse(null);
        System.out.println("founded uni" + universite);
        if (universite == null) {
            throw new RuntimeException("Universite not found");
        }
        if (universite.getFoyer() == null) {
            throw new RuntimeException("There is no foyer affected to this universite");
        }
        Foyer foundedFoyer = foyerRepo.findById(universite.getFoyer().getId()).orElse(null);
        System.out.println("founded foyer: " + foundedFoyer);
        foundedFoyer.setUniversite(null);
        Foyer savedFoyer = foyerRepo.save(foundedFoyer);
        entityManager.clear(); // Clearing the Hibernate session ensures that subsequent entity queries fetch the latest data from the database.
        System.out.println("saved foyer♦: " + savedFoyer);
        Universite savedUniversity = universiteRepo.findById(idUniversite).orElse(null);
        System.out.println("saved university: " + savedUniversity);
        return savedUniversity;
    }
    @Override
    public void removeUniversity(long id) {
        universiteRepo.deleteById(id);
    }

    @Override
    public List<Universite> getUniversitiesByAddress(String adresse) {
        return universiteRepo.findUniversitesByAdresseIsLike(adresse);
    }

    @Override
    public List<Universite> getUniversitiesByNom(String nom) {
        return universiteRepo.findUniversitesByNomIsLike(nom);
    }

    @Override
    public List<Universite> getUniversitiesByNomFoyer(String nom) {
        return universiteRepo.findUniversitesByFoyer(nom);
    }
    @Override
    public List<Universite> getUniversitiesSearch(String nom,String add) {
        return universiteRepo.findUniversitesSearch(nom,add);
    }
}
