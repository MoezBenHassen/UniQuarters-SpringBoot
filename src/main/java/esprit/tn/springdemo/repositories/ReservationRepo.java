package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Repository

public interface ReservationRepo extends JpaRepository<Reservation, Long> {


    Reservation findById(String idReservation);
    List<Reservation> findReservationsByAnneeUniversitaireBetween(Date anneeUniversitaire1, Date anneeUniversitaire2);

    @Query("SELECT r FROM Reservation r JOIN r.etudiants e WHERE e.id = ?1")
    Set<Reservation> findReservationsByEtudiants(long idEtudiant);

    long countReservationsByAnneeUniversitaireBetween(Date anneeUniversitaire1, Date anneeUniversitaire2);
}
