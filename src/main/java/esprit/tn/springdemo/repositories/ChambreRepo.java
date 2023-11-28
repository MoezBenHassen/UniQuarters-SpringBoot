package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChambreRepo extends JpaRepository<Chambre, Long> {
    //List<Chambre> findChambresByBloc_Nom(String nom);
    List<Chambre> findChambresByBloc_Nom(String nom);

    Chambre findChambreByReservations(Reservation reservation);
    @Query("select Chambre from Chambre join Reservation resa where resa.anneeUniversitaire between :debut and :fin")
    List<Chambre> findChambresByReservations(@Param("debut") LocalDate dateDebut, @Param("fin") LocalDate dateFin);
}
