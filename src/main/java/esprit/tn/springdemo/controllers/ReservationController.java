package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.Reservation;
import esprit.tn.springdemo.repositories.BlocRepo;
import esprit.tn.springdemo.repositories.ChambreRepo;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RequestMapping("reservations")
public class ReservationController {
    private final IReservationService reservationService;
    /*private final BlocRepo blocRepo;
    private final ChambreRepo chambreRepo;*/

    @GetMapping
    public ResponseEntity<ApiResponse> getReservations() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Reservation> reservations = reservationService.retrieveAllReservation();
            apiResponse.setResponse(HttpStatus.OK, "Reservations retrieved");
            apiResponse.addData("reservations", reservations);
            // get chambre for each reservation
            List<Map<String, Object>> chambres = new ArrayList<>();
            reservations.forEach(reservation -> {
                Map<String, Object> reservationDetails = reservationService.getReservationDetails(reservation);
                Chambre chambre = (Chambre) reservationDetails.get("chambre");

                // Create a map representing a single reservation with chambre details
                Map<String, Object> reservationMap = new HashMap<>();
                reservationMap.put("idReservation", reservation.getId());
                reservationMap.putAll(filterChambreAttributes(chambre));

                // Add the reservation map to the list
                chambres.add(reservationMap);
            });
            apiResponse.addData("chambres", chambres);
            // get bloc for each chambre
            /*List<Map<String, Object>> blocs = new ArrayList<>();
            chambres.forEach(chambre -> {
                System.out.println("chambre: " + chambre);
                Bloc bloc = blocRepo.findBlocByChambres(chambreRepo.getById((Long) chambre.get("id")));
                Map<String, Object> blocMap = new HashMap<>();
                blocMap.put("idChambre", chambre.get("id"));

                blocMap.put("nom", bloc.getNom());
                blocMap.put("capacite", bloc.getCapacite());
                blocMap.put("foyer", bloc.getFoyer());
                blocs.add(blocMap);
            });
            apiResponse.addData("blocs", blocs);*/
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/{idReservation}")
    public ResponseEntity<ApiResponse> updateReservation(@RequestBody Reservation res, @PathVariable String idReservation) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Reservation foundReservation = reservationService.retrieveReservation(idReservation);
            if (foundReservation == null) {
                throw new RuntimeException("Reservation not found");
            }
            res.setId(idReservation);
            Reservation updatedReservation = reservationService.updateReservation(res);
            apiResponse.setResponse(HttpStatus.OK, "Reservation updated");
            apiResponse.addData("reservation", updatedReservation);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    private Map<String, Object> filterChambreAttributes(Chambre chambre) {
        Map<String, Object> filteredChambre = new HashMap<>();
        filteredChambre.put("id", chambre.getId());
        filteredChambre.put("numero", chambre.getNumero());
        filteredChambre.put("type", chambre.getType());
        filteredChambre.put("bloc", chambre.getBloc());
        // Add other attributes as needed...
        return filteredChambre;
    }

    @GetMapping("/{idReservation}")
    public ResponseEntity<ApiResponse> getReservation(@PathVariable String idReservation) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Reservation reservation = reservationService.retrieveReservation(idReservation);
            if (reservation == null) {
                throw new RuntimeException("Reservation not found");
            }
            Map<String, Object> reservationDetails = reservationService.getReservationDetails(reservation);
            Chambre chambre = (Chambre) reservationDetails.get("chambre");
            Set<Etudiant> etudiants = (Set<Etudiant>) reservationDetails.get("etudiants");
            apiResponse.setResponse(HttpStatus.OK, "Reservation retrieved");
            apiResponse.addData("reservation", reservation);
            apiResponse.addData("chambre", filterChambreAttributes(chambre));
            //apiResponse.addData("etudiants", etudiants);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/valider/{idReservation}")
    public ResponseEntity<ApiResponse> validerReservation(@PathVariable String idReservation) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Reservation foundReservation = reservationService.retrieveReservation(idReservation);
            if (foundReservation == null) {
                throw new RuntimeException("Reservation not found");
            }
            Reservation updatedReservation = reservationService.validerReservation(idReservation);
            apiResponse.setResponse(HttpStatus.OK, "Reservation validée");
            apiResponse.addData("reservation", updatedReservation);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PostMapping("/{idChambre}/{cinEtudiant}")
    public ResponseEntity<ApiResponse> ajouterReservation(@PathVariable long idChambre, @PathVariable long cinEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Reservation reservation = new Reservation();
            reservation.setAnneeUniversitaire(java.time.LocalDate.now().withDayOfYear(1));
            reservation.setEstValide(false);
            Reservation addedReservation = reservationService.ajouterReservation(reservation, idChambre, cinEtudiant);
            apiResponse.setResponse(HttpStatus.OK, "Reservation added");
            apiResponse.addData("reservation", addedReservation);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @DeleteMapping("/{cinEtudiant}")
    public ResponseEntity<ApiResponse> annulerReservation(@PathVariable long cinEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Reservation annulerReservation = reservationService.annulerReservation(cinEtudiant);
            apiResponse.setResponse(HttpStatus.OK, "Reservation annulée");
            apiResponse.addData("reservation", annulerReservation);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
}
