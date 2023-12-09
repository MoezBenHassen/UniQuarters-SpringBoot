package esprit.tn.springdemo.services;

import esprit.tn.springdemo.dto.ChambreDTO;
import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.Reservation;
import esprit.tn.springdemo.entities.TypeChambre;
import esprit.tn.springdemo.repositories.BlocRepo;
import esprit.tn.springdemo.repositories.ChambreRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChambreServiceImpl implements IChambreService {
    ChambreRepo chambreRepo;
    BlocRepo blocRepo;

    @Override
    public List<Chambre> retrieveAllChambres() {
        return chambreRepo.findAll();
    }

    @Override
    public Chambre addChambre(Chambre c) {
        return chambreRepo.save(c);
    }

    @Override
    public Chambre updateChambre(Chambre c) {
        return chambreRepo.save(c);
    }

    @Override
    public Chambre retrieveChambre(long idChambre) {
        return chambreRepo.findById(idChambre).orElse(null);
    }

    @Override
    public List<Chambre> getChambreByReservationAnneeUniversitaire(LocalDate dateDebut, LocalDate dateFin) {
        return chambreRepo.findAll()
                .stream()
                .filter(chambre -> chambre.getReservations().stream()
                        .anyMatch(r -> r.getAnneeUniversitaire().isAfter(dateDebut) && r.getAnneeUniversitaire().isBefore(dateFin)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Chambre> getCChambresByNomBloc(String nom) {
        return chambreRepo.findChambresByBloc_Nom(nom);
    }

    @Override
    public Chambre afftecterChambreABloc(long idChambre, String nomBloc) {
        System.out.println("Before affecting chambre with id " + idChambre + " to bloc with nom " + nomBloc);

        Chambre chambre = chambreRepo.findById(idChambre).orElseThrow(() -> new RuntimeException("Chambre not found"));
        System.out.println("Found chambre: " + chambre);

        Bloc bloc = blocRepo.findByNom(nomBloc);
        System.out.println("Found bloc: " + bloc);
        if (bloc == null) {
            throw new RuntimeException("Bloc not found");
        }

        chambre.setBloc(bloc);
        Chambre savedChambre = chambreRepo.save(chambre);
        return savedChambre;
    }
    @Override
    public List<Chambre> getAvailableChambres() {
        return chambreRepo.findAll()
                .stream()
                .filter(Chambre::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Chambre> getChambresByType(TypeChambre chambreType) {
        return chambreRepo.findChambresByType(chambreType);
    }

    @Override
    public List<Chambre> getChambresWithReservations() {
        return chambreRepo.findAllWithReservations();
    }
    @Override
    public void deleteChambre(long id) {
        chambreRepo.deleteById(id);
    }

//    @Override
//    public List<ChambreDTO> getChambresWithDetails() {
//        return chambreRepo.findAll()
//                .stream()
//                .map(this::mapChambreToDTO)
//                .collect(Collectors.toList());
//    }
//
//    private ChambreDTO mapChambreToDTO(Chambre chambre) {
//        ChambreDTO dto = new ChambreDTO();
//        dto.setId(chambre.getId());
//        dto.setChambreNumber(chambre.getChambreNumber());
//        dto.setCapacity(chambre.getCapacity());
//        dto.setDescription(chambre.getDescription());
//        dto.setChambreType(chambre.getChambreType());
//
//        Bloc bloc = chambre.getBloc();
//        if (bloc != null) {
//            dto.setBlocNom(bloc.getNom());
//            Foyer foyer = bloc.getFoyer();
//            if (foyer != null) {
//                dto.setFoyerNom(foyer.getNom());
//                Universite universite = foyer.getUniversite();
//                if (universite != null) {
//                    dto.setUniversiteNom(universite.getNom());
//                }
//            }
//        }
//
//        return dto;
//    }
}
