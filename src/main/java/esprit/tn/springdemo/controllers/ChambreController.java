package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.dto.ChambreDTO;
import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.TypeChambre;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IChambreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/chambres")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.DELETE})

public class ChambreController {

    private final IChambreService iChambreService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> retrieveChambres(@RequestParam(required = false) String nomBloc) {
        ApiResponse apiResponse = new ApiResponse();
        List<Chambre> chambres;

        try {
            if (nomBloc != null && !nomBloc.isEmpty()) {
                chambres = iChambreService.getCChambresByNomBloc(nomBloc);
            } else {
                chambres = iChambreService.retrieveAllChambres();
            }

            apiResponse.setResponse(HttpStatus.OK, "Chambres retrieved");
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
        if (c.getReservations() == null) {
            c.setReservations(new HashSet<>());
        }
        Chambre addedChambre = iChambreService.addChambre(c);
        addedChambre.updateAvailability();

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

        if (c.getReservations() == null) {
            c.setReservations(new HashSet<>());
        }

        c.setId(idChambre);

        Chambre updatedChambre = iChambreService.updateChambre(c);
        updatedChambre.setCapacity(c.getCapacity());
        updatedChambre.updateAvailability();

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

//    @GetMapping("/details")
//    public ResponseEntity<ApiResponse> getChambresWithDetails() {
//        ApiResponse apiResponse = new ApiResponse();
//        try {
//            List<ChambreDTO> chambresWithDetails = iChambreService.getChambresWithDetails();
//            apiResponse.setResponse(HttpStatus.OK, "Chambres with details retrieved");
//            apiResponse.addData("chambres", chambresWithDetails);
//        } catch (Exception e) {
//            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
//    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableChambres() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Chambre> availableChambres = iChambreService.getAvailableChambres();
            apiResponse.setResponse(HttpStatus.OK, "Available chambres retrieved");
            apiResponse.addData("chambres", availableChambres);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/byType")
    public ResponseEntity<ApiResponse> getChambresByType(@RequestParam TypeChambre type) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Chambre> chambresByType = iChambreService.getChambresByType(type);
            apiResponse.setResponse(HttpStatus.OK, "Chambres by type retrieved");
            apiResponse.addData("chambres", chambresByType);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/withReservations")
    public ResponseEntity<ApiResponse> getChambresWithReservations() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Chambre> chambresWithReservations = iChambreService.getChambresWithReservations();
            apiResponse.setResponse(HttpStatus.OK, "Chambres with reservations retrieved");
            apiResponse.addData("chambres", chambresWithReservations);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @DeleteMapping("/{idChambre}")
    public ResponseEntity<ApiResponse> deleteChambre(@PathVariable long idChambre) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            iChambreService.deleteChambre(idChambre);
            apiResponse.setResponse(HttpStatus.OK, "Chambre deleted");
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
}