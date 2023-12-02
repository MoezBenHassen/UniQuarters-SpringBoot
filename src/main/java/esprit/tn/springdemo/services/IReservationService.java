package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Reservation;

import java.util.List;
import java.util.Map;

public interface IReservationService {
    List<Reservation> retrieveAllReservation();

    public Reservation updateReservation(Reservation updatedReservation, long idChambre, long cinEtudiant);
    Reservation retrieveReservation(String idReservation);

    public Reservation ajouterReservation(Reservation reservation, long idChambre, long cinEtudiant);

    public Reservation validerReservation(String idReservation);

    public Reservation annulerReservation(long cinEtudiant);

    Map<String, Object> getReservationDetails(Reservation reservation);

    public List<Map<String, Object>> countChambresReservations();
}
