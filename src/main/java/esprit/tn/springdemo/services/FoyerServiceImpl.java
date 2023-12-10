package esprit.tn.springdemo.services;

import esprit.tn.springdemo.dto.FoyerDTO;
import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Foyer;
import esprit.tn.springdemo.entities.Universite;
import esprit.tn.springdemo.repositories.BlocRepo;
import esprit.tn.springdemo.repositories.FoyerRepo;
import esprit.tn.springdemo.repositories.UniversiteRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FoyerServiceImpl implements IFoyerService {
    private final FoyerRepo foyerRepo;
    private final UniversiteRepo uniRepo;
    private ModelMapper modelMapper;

    @Override
    public FoyerDTO mapToDTO(Foyer foyer) {
        FoyerDTO foyerDTO = new FoyerDTO();
        foyerDTO.setUniversite(foyer.getUniversite());
        foyerDTO.setNom(foyer.getNom());
        foyerDTO.setCapacite(foyer.getCapacite());
        foyerDTO.setId(foyer.getId());
        foyerDTO.setBlocs(foyer.getBlocs().stream().map(Bloc::getId).collect(Collectors.toList()));
        return foyerDTO;
    }

    @Override
    public List<Foyer> retrieveAllFoyers() {
        return foyerRepo.findAll();
    }

    @Override
    public Foyer addFoyer(Foyer f) {
        return foyerRepo.save(f);
    }

    @Override
    public Foyer updateFoyer(Foyer f) {
        return foyerRepo.save(f);
    }

    @Override
    public Foyer retrieveFoyer(long idFoyer) {
        return foyerRepo.findById(idFoyer).orElse(null);
    }

    @Override
    public void removeFoyer(long idFoyer) {
        foyerRepo.deleteById(idFoyer);
    }

    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        Universite u = uniRepo.findById(idUniversite).orElse(null);
        foyer.setUniversite(u);
        for (Bloc b : foyer.getBlocs()) {
            b.setFoyer(foyer);
        }
        return foyerRepo.save(foyer);
    }
}
