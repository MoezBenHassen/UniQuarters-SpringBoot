package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.Reservation;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("reservations")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.DELETE})

public class ReservationController {
    private final IReservationService reservationService;

    @GetMapping
    public ResponseEntity<ApiResponse> getReservations() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Reservation> reservations = reservationService.retrieveAllReservation();
            apiResponse.setResponse(HttpStatus.OK, "Reservations retrieved");
            apiResponse.addData("reservations", reservations);
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

    @GetMapping("/{idReservation}")
    public ResponseEntity<ApiResponse> getReservation(@PathVariable String idReservation) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Reservation reservation = reservationService.retrieveReservation(idReservation);
            apiResponse.setResponse(HttpStatus.OK, "Reservation retrieved");
            apiResponse.addData("reservation", reservation);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
}
