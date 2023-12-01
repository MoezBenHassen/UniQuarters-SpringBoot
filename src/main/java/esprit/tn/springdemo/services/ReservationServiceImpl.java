package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.Reservation;
import esprit.tn.springdemo.repositories.ChambreRepo;
import esprit.tn.springdemo.repositories.EtudiantRepo;
import esprit.tn.springdemo.repositories.ReservationRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    private final ReservationRepo reservationRepo;
    private final ChambreRepo chambreRepo;
    private final EtudiantRepo etudiantRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Reservation> retrieveAllReservation() {
        return reservationRepo.findAll();
    }

    @Override
    public Reservation updateReservation(Reservation res) {
        return reservationRepo.save(res);
    }

    @Override
    public Reservation retrieveReservation(String idReservation) {
        return reservationRepo.findById(idReservation);
    }

    @Override
    public Reservation ajouterReservation(Reservation reservation, long idChambre, long cinEtudiant) {
        System.out.println("Begin add reservation");
        Chambre chambre = chambreRepo.getById(idChambre);
        System.out.println("founded chambre: " + chambre);
        if (chambre == null) {
            throw new RuntimeException("Chambre not found");
        }

        Etudiant etudiant = etudiantRepo.getByCin(cinEtudiant);
        System.out.println("founded etudiant: " + etudiant);
        if (etudiant == null) {
            throw new RuntimeException("Etudiant not found");
        }

        System.out.println("Bloc: " + chambre.getBloc());
        if (chambre.getBloc() == null) {
            throw new RuntimeException("Bloc not found");
        }
        String id = this.generateId(idChambre, chambre.getBloc().getNom(), cinEtudiant);
        System.out.println("generated id: " + id);

        // check if reservation already exists
        Reservation existingReservation = reservationRepo.findById(id);
        if (existingReservation != null) {
            Boolean resaIsValide = existingReservation.getEstValide();
            if (resaIsValide == true) {
                throw new RuntimeException("Reservation already exists and it is valid");
            }
            System.out.println("reservation already exists but it is not valid");
            if (chambre.getReservations().contains(existingReservation)) {
                throw new RuntimeException("Chambre already contains this reservation");
            }
            if (etudiant.getReservations().contains(existingReservation)) {
                throw new RuntimeException("Etudiant already contains this reservation");
            }

        }
        reservation.setId(id);
        //System.out.println("les etudiants de entite resa: " + reservation.getEtudiants());
        System.out.println("les reservations de entite etudiant: " + etudiant.getReservations());
        System.out.println("saving reservation: " + reservation);
        reservationRepo.save(reservation);

        System.out.println("adding reservation to chambre");

        chambre.getReservations().add(reservation);
        Chambre savedChambre = chambreRepo.save(chambre);
        System.out.println("saved chambre: " + savedChambre);

        System.out.println("adding reservation to etudiant");
        etudiant.getReservations().add(reservation);
        Etudiant savedEtudiant = etudiantRepo.save(etudiant);
        System.out.println("saved etudiant: " + savedEtudiant);

        entityManager.clear();
        Reservation savedReservation = reservationRepo.findById(id);
        System.out.println("saved reservation: " + savedReservation);
        return savedReservation;
    }

    @Override
    public Reservation validerReservation(String idReservation) {
        Reservation reservation = reservationRepo.findById(idReservation);
        System.out.println("founded reservation: " + reservation);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }
        reservation.setEstValide(true);
        System.out.println("saving reservation: " + reservation);
        Reservation savedReservation = reservationRepo.save(reservation);
        entityManager.clear();
        return reservationRepo.findById(savedReservation.getId());
    }

    @Override
    public Reservation annulerReservation(long cinEtudiant) {
        Etudiant etudiant = etudiantRepo.getByCin(cinEtudiant);
        System.out.println("founded etudiant: " + etudiant);
        if (etudiant == null) {
            throw new RuntimeException("Etudiant not found");
        }
        System.out.println("les reservations de etudiant: " + etudiant.getReservations());
        Reservation reservation = etudiant.getReservations().get(0);
        System.out.println("founded reservation: " + reservation);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }
        etudiant.getReservations().remove(reservation);
        Etudiant savedEtudiant = etudiantRepo.save(etudiant);
        System.out.println("saved etudiant: " + savedEtudiant);

        // desaffecter reservation de chambre
        Chambre chambre = chambreRepo.findChambreByReservations(reservation);
        System.out.println("founded chambre: " + chambre);
        System.out.println("les reservations de chambre: " + chambre.getReservations());
        if (chambre == null) {
            throw new RuntimeException("Chambre not found");
        }
        chambre.getReservations().remove(reservation);
        Chambre savedChambre = chambreRepo.save(chambre);
        System.out.println("saved chambre: " + savedChambre);

        reservation.setEstValide(false);
        Reservation savedReservation = reservationRepo.save(reservation);
        entityManager.clear();

        return reservationRepo.findById(savedReservation.getId());
    }

    private String generateId(long idChambre, String nomBloc, long cinEtudiant) {
        return idChambre + "-" + nomBloc + "-" + cinEtudiant;
    }


}
