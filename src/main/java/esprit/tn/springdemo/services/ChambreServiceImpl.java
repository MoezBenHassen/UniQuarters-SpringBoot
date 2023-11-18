package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.Reservation;
import esprit.tn.springdemo.repositories.BlocRepo;
import esprit.tn.springdemo.repositories.ChambreRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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
        List<Chambre> chambres = null;
        for (Chambre c : chambreRepo.findAll()) {
            for (Reservation r : c.getReservations()) {
                if (r.getAnneeUniversitaire().isAfter(dateDebut) && r.getAnneeUniversitaire().isBefore(dateFin)) {
                    //return chambreRepo.findAll();
                    chambres.add(c);
                }
            }
        }
        return chambres;
    }

    @Override
    public List<Chambre> getCChambresByNomBloc(String nom) {
        return chambreRepo.findChambresByBloc_Nom(nom);
    }

    @Override
    public Chambre afftecterChambreABloc(long idChambre, String nomBloc) {
        System.out.println("Before affecting chambre with id " + idChambre + " to bloc with nom " + nomBloc);
        System.out.println("Searching for chambre with id " + idChambre);
        Chambre chambre = chambreRepo.findById(idChambre).orElse(null);
        System.out.println("Found chambre: " + chambre);
        if (chambre == null) {
            throw new RuntimeException("Chambre not found");
        }
        Bloc bloc = blocRepo.findByNom(nomBloc);
        System.out.println("Found bloc: " + bloc);
        if (bloc == null) {
            throw new RuntimeException("Bloc not found");
        }
        chambre.setBloc(bloc);
        Chambre savedChambre = chambreRepo.save(chambre);
        return savedChambre;
    }
}
