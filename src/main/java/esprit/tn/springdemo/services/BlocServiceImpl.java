package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.repositories.BlocRepo;
import esprit.tn.springdemo.repositories.ChambreRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BlocServiceImpl implements IBlocService {
    private final BlocRepo blocRepo;
    private final ChambreRepo chambreRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Bloc> retrieveBlocs() {
        return blocRepo.findAll();
    }

    @Override
    public Bloc updateBloc(Bloc bloc) {
        return blocRepo.save(bloc);
    }

    @Override
    public Bloc addBloc(Bloc bloc) {
        return blocRepo.save(bloc);
    }

    @Override
    public Bloc retrieveBloc(long idBloc) {
        return blocRepo.findById(idBloc).orElse(null);
    }

    @Override
    public void removeBloc(long idBloc) {
        blocRepo.deleteById(idBloc);
    }

    @Override
    public Bloc affectChambres(long blocId, List<Chambre> chambres) {
        Bloc bloc = blocRepo.findById(blocId).orElse(null);
        List<Chambre> savedChambres = null;
        if (bloc == null) {
            throw new RuntimeException("Bloc not found");
        }
        for (Chambre chambre : chambres) {
            Chambre chambreToUpdate = chambreRepo.findById(chambre.getId()).orElse(null);
            chambreToUpdate.setBloc(bloc);
            chambreRepo.save(chambreToUpdate);
            //Chambre savedChambre = chambreRepo.findById(chambre.getId()).orElse(null)
            //savedChambres.add(savedChambre);
        }
        entityManager.clear();
        Bloc updatedBloc = blocRepo.findById(blocId).orElse(null);
        return updatedBloc;
    }
}
