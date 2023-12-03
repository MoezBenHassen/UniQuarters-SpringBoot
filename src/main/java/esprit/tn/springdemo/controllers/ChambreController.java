package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IChambreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/chambres")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
public class ChambreController {
    private final IChambreService iChambreService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> retrieveChambres(@RequestParam(required = false) String nomBloc) {
        ApiResponse apiResponse = new ApiResponse();
        List<Chambre> chambres;
        Boolean isNomBlocProvided = nomBloc != null && !nomBloc.isEmpty();
        try {
            if (isNomBlocProvided) {
                chambres = iChambreService.getCChambresByNomBloc(nomBloc);
            } else {
                chambres = iChambreService.retrieveAllChambres();
            }
            Boolean isChambresEmpty = chambres == null || chambres.isEmpty();
            String message = !isNomBlocProvided ? "Chambres retrieved" : "Chambres retrieved by bloc";
            HttpStatus httpStatus = (isChambresEmpty) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
            if (isChambresEmpty) {
                message += " (empty)";
            }
            apiResponse.setResponse(httpStatus, message);
            apiResponse.addData("chambres", chambres);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }


    @PostMapping("")
    public ResponseEntity<ApiResponse> addChambre(@RequestBody Chambre c) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Chambre addedChambre = iChambreService.addChambre(c);
            //throw new RuntimeException("Test exception");
            apiResponse.setResponse(org.springframework.http.HttpStatus.CREATED, "Chambre added");
            apiResponse.addData("chambre", addedChambre);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/{idChambre}")
    public ResponseEntity<ApiResponse> updateChambre(@RequestBody Chambre c, @PathVariable long idChambre) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Chambre foundChambre = iChambreService.retrieveChambre(idChambre);
            if (foundChambre == null) {
                throw new RuntimeException("Chambre not found");
            }
            c.setId(idChambre);
            Chambre updatedChambre = iChambreService.updateChambre(c);
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Chambre updated");
            apiResponse.addData("chambre", updatedChambre);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/{idChambre}")
    public ResponseEntity<ApiResponse> retrieveChambre(@PathVariable long idChambre) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Chambre chambre = iChambreService.retrieveChambre(idChambre);
            if (chambre == null) {
                throw new RuntimeException("Chambre not found");
            }
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Chambre retrieved");
            apiResponse.addData("chambre", chambre);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }


    @PostMapping("/affecterABloc/{idChambre}/{nomBloc}")
    public ResponseEntity<ApiResponse> afftecterChambreABloc(@PathVariable long idChambre, @PathVariable String nomBloc) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Chambre chambre = iChambreService.afftecterChambreABloc(idChambre, nomBloc);
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Chambre affected");
            apiResponse.addData("chambre", chambre);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    /*@GetMapping("ChambreByReservationAnneeUniversitaire/{dateDebut}/{dateFin}")
    public List<Chambre> getChambreByReservationAnneeUniversitaire(@PathVariable Date dateDebut, @PathVariable Date dateFin) {
        return iChambreService.getChambreByReservationAnneeUniversitaire(dateDebut, dateFin);
    }*
     */
    @GetMapping("byReservationAnneeUniversitaire/{dateDebut}/{dateFin}")
    public ResponseEntity<ApiResponse> getChambreByReservationAnneeUniversitaire(@PathVariable LocalDate dateDebut, @PathVariable LocalDate dateFin) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Chambre> chambres = iChambreService.getChambreByReservationAnneeUniversitaire(dateDebut, dateFin);
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Chambres retrieved");
            apiResponse.addData("chambres", chambres);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
}
