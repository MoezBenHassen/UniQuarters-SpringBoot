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
        Etudiant etudiant = etudiantRepo.getByCin(cinEtudiant);
        System.out.println("founded etudiant: " + etudiant);

        String id = this.generateId(idChambre, cinEtudiant, chambre.getReservations().size() + 1);
        System.out.println("generated id: " + id);
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

    private String generateId(long idChambre, long cinEtudiant, long index) {
        return idChambre + "-" + cinEtudiant + "-" + index;
    }
}
