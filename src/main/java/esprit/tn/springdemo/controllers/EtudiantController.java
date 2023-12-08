package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.authentication.AuthenticationService;
import esprit.tn.springdemo.authentication.RegisterRequest;
import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IEtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
@AllArgsConstructor
public class EtudiantController {
    private final IEtudiantService iEtudiantService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> register(@RequestBody Etudiant etudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(HttpStatus.CREATED, "Etudiant added");
            apiResponse.addData("etudiant", iEtudiantService.addEtudiant(etudiant));
        } catch (Exception ex) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getEtudiants() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Etudiant> etudiants = iEtudiantService.retrieveAllEtudiants();
            apiResponse.setResponse(HttpStatus.OK, "Etudiants retrieved");
            apiResponse.addData("etudiants", etudiants);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PostMapping("/addMultiple")
    public ResponseEntity<ApiResponse> addEtudiants(@RequestBody List<Etudiant> etudiants) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Etudiant> addedEtudiants = iEtudiantService.addEtudiants(etudiants);
            //throw new RuntimeException("Test exception");
            apiResponse.setResponse(HttpStatus.CREATED, "Etudiants added");
            apiResponse.addData("etudiants", addedEtudiants);
        } catch (Exception ex) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/{idEtudiant}")
    public ResponseEntity<ApiResponse> updateEtudiant(@RequestBody Etudiant etudiant, @PathVariable long idEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Etudiant foundEtudiant = iEtudiantService.retrieveEtudiant(idEtudiant);
            if (foundEtudiant == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "Etudiant not found");
            } else {
                etudiant.setId(idEtudiant);
                Etudiant updatedEtudiant = iEtudiantService.updateEtudiant(etudiant);
                apiResponse.setResponse(HttpStatus.OK, "Etudiant updated");
                apiResponse.addData("etudiant", updatedEtudiant);
            }

        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/{idEtudiant}")
    public ResponseEntity<ApiResponse> getEtudiant(@PathVariable long idEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Etudiant etudiant = iEtudiantService.retrieveEtudiant(idEtudiant);
            if (etudiant == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "Etudiant not found");
            } else {
                apiResponse.setResponse(HttpStatus.OK, "Etudiant retrieved");
                apiResponse.addData("etudiant", etudiant);
            }
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @DeleteMapping("/{idEtudiant}")
    public ResponseEntity<ApiResponse> deleteEtudiant(@PathVariable long idEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Etudiant etudiant = iEtudiantService.retrieveEtudiant(idEtudiant);
            if (etudiant == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "Etudiant not found");
                return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
            } else {
                iEtudiantService.removeEtudiant(idEtudiant);
                apiResponse.setResponse(HttpStatus.OK, "Etudiant deleted");
            }
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

}
