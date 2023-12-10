package esprit.tn.springdemo.services;

import esprit.tn.springdemo.dto.ChambreDTO;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.TypeChambre;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface IChambreService {

    List<Chambre> retrieveAllChambres();

    Chambre retrieveChambre(long idChambre);

    Chambre addChambre(Chambre chambre);

    Chambre updateChambre(Chambre chambre);

    Chambre afftecterChambreABloc(long idChambre, String nomBloc);

    List<Chambre> getChambreByReservationAnneeUniversitaire(LocalDate dateDebut, LocalDate dateFin);

    List<Chambre> getCChambresByNomBloc(String nomBloc);

//    List<ChambreDTO> getChambresWithDetails();

    List<Chambre> getAvailableChambres();

    List<Chambre> getChambresByType(TypeChambre type);

    List<Chambre> getChambresWithReservations();

    void deleteChambre(long id);

}
