package esprit.tn.springdemo.repositories;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.TypeChambre;
import esprit.tn.springdemo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ChambreRepo extends JpaRepository<Chambre, Long> {
    List<Chambre> findChambresByBloc_Nom(String nom);

    Chambre findChambreByReservations(Reservation reservation);
    List<Chambre> findChambresByType(TypeChambre type);

    @Query("SELECT DISTINCT c FROM Chambre c LEFT JOIN FETCH c.reservations")
    List<Chambre> findAllWithReservations();
}
