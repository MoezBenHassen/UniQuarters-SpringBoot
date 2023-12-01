package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.Universite;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IFoyerService;
import esprit.tn.springdemo.services.IUniversiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/universites")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.DELETE})

public class UniversiteController {
    private final IUniversiteService universiteService;
    private final IFoyerService foyerService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getUniversities() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Universities retrieved");
            apiResponse.addData("universities", universiteService.retrieveAllUniversities());
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addUniversity(@RequestBody Universite universite) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            System.out.println(universite);
            apiResponse.setResponse(org.springframework.http.HttpStatus.CREATED, "University added");
            apiResponse.addData("university", universiteService.addUniversity(universite));
            apiResponse.addData("foyer", foyerService.addFoyer(universite.getFoyer()));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/{idUnivesity}")
    public ResponseEntity<ApiResponse> updateUniversity(@RequestBody Universite universite, @PathVariable long idUnivesity) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.CREATED, "University updated");
            universite.setId(idUnivesity);
            apiResponse.addData("university", universiteService.updateUniversity(universite,idUnivesity));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @DeleteMapping ("/{idUniversity}")
    public ResponseEntity<ApiResponse> deleteUniversity(@PathVariable long idUniversity) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "University deleted");
            universiteService.removeUniversity(idUniversity);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
    @GetMapping("/{idUniversity}")
    public ResponseEntity<ApiResponse> getUniversity(@PathVariable long idUniversity) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "University retrieved");
            apiResponse.addData("university", universiteService.retrieveUniversity(idUniversity));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
    @GetMapping("/filtre/{adresse}")
    public ResponseEntity<ApiResponse> getUniversities(@PathVariable String adresse) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Universities retrieved");
            apiResponse.addData("universities", universiteService.getUniversitiesByAddress(adresse));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse> getUniversitiesByNom(@PathVariable String nom) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Universities retrieved");
            apiResponse.addData("universities", universiteService.getUniversitiesByNom(nom));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
    @GetMapping("/search/{nom}/{add}")
    public ResponseEntity<ApiResponse> getUniversitiesSearch(@PathVariable String nom,@PathVariable String add) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Universities retrieved");
            apiResponse.addData("universities", universiteService.getUniversitiesSearch(nom,add));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
    @GetMapping("/foyerNom/{nom}")
    public ResponseEntity<ApiResponse> getUniversitiesByFoyer(@PathVariable String nom) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Universities retrieved");
            apiResponse.addData("universities", universiteService.getUniversitiesByNomFoyer(nom));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }
    @PutMapping("/affectFoyer/{idUniversity}/{idFoyer}")
    public ResponseEntity<ApiResponse> affectFoyer(@PathVariable long idUniversity, @PathVariable long idFoyer) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Universite universite = universiteService.affectFoyer(idUniversity, idFoyer);
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Foyer affected");
            apiResponse.addData("university", universite);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/desaffectFoyer/{idUniversity}")
    public ResponseEntity<ApiResponse> desaffectFoyer(@PathVariable long idUniversity) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Universite universite = universiteService.desaffecterFoyerAUniversite(idUniversity);
            System.out.println("after desaffecting service: " + universite);
            apiResponse.setResponse(org.springframework.http.HttpStatus.OK, "Foyer desaffected");
            apiResponse.addData("university", universite);
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }


}