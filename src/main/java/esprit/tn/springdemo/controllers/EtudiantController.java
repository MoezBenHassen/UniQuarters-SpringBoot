package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IEtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class EtudiantController {
    private final IEtudiantService iEtudiantService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getEtudiants() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Etudiant> etudiants = iEtudiantService.retrieveAllEtudiants();
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Etudiants retrieved");
            apiResponse.addData("etudiants", etudiants);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addEtudiants(@RequestBody List<Etudiant> etudiants) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Etudiant> addedEtudiants = iEtudiantService.addEtudiants(etudiants);
            //throw new RuntimeException("Test exception");
            apiResponse.setResponse(org.springframework.http.HttpStatus.CREATED, "Etudiants added");
            apiResponse.addData("etudiants", addedEtudiants);
        } catch (Exception ex) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/{idEtudiant}")
    public ResponseEntity<ApiResponse> updateEtudiant(@RequestBody Etudiant etudiant, @PathVariable long idEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Etudiant foundEtudiant = iEtudiantService.retrieveEtudiant(idEtudiant);
            if (foundEtudiant == null) {
                apiResponse.setResponse(org.springframework.http.HttpStatus.NOT_FOUND, "Etudiant not found");
            } else {
                etudiant.setId(idEtudiant);
                Etudiant updatedEtudiant = iEtudiantService.updateEtudiant(etudiant);
                apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Etudiant updated");
                apiResponse.addData("etudiant", updatedEtudiant);
            }

        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/{idEtudiant}")
    public ResponseEntity<ApiResponse> getEtudiant(@PathVariable long idEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Etudiant etudiant = iEtudiantService.retrieveEtudiant(idEtudiant);
            if (etudiant == null) {
                apiResponse.setResponse(org.springframework.http.HttpStatus.NOT_FOUND, "Etudiant not found");
            } else {
                apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Etudiant retrieved");
                apiResponse.addData("etudiant", etudiant);
            }
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @DeleteMapping("/{idEtudiant}")
    public ResponseEntity<ApiResponse> deleteEtudiant(@PathVariable long idEtudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Etudiant etudiant = iEtudiantService.retrieveEtudiant(idEtudiant);
            if (etudiant == null) {
                apiResponse.setResponse(org.springframework.http.HttpStatus.NOT_FOUND, "Etudiant not found");
                return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
            } else {
                iEtudiantService.removeEtudiant(idEtudiant);
                apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Etudiant deleted");
                //apiResponse.addData("etudiant", etudiant);
            }
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

}
