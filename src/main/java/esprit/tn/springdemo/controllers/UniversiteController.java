package esprit.tn.springdemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.tn.springdemo.entities.Universite;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IFoyerService;
import esprit.tn.springdemo.services.IUniversiteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/universites")
@AllArgsConstructor
@Slf4j
public class UniversiteController {
    private final String directory="C:\\uploadedFiles\\img\\";
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
    public ResponseEntity<ApiResponse> addUniversity(@RequestParam("logo") MultipartFile logo,@RequestParam("universite") String u) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            log.info(u);
            File dir = new File(directory);
            dir.mkdirs();
            Universite universite= new ObjectMapper().readValue(u,Universite.class);
            String fileName = UUID.randomUUID().toString() + "_" + logo.getOriginalFilename();
            Path targetLocation = Paths.get(directory).resolve(fileName);
            Files.copy(logo.getInputStream(), targetLocation);
            universite.setImage(fileName);
            apiResponse.setResponse(org.springframework.http.HttpStatus.CREATED, "University added");
            apiResponse.addData("university", universiteService.addUniversity(universite));
            apiResponse.addData("foyer", foyerService.addFoyer(universite.getFoyer()));
        } catch (Exception e) {
            apiResponse.setResponse(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("/{idUnivesity}")
    public ResponseEntity<ApiResponse> updateUniversity(@RequestParam(value = "logo", required = false) MultipartFile logo,@RequestParam("universite") String u, @PathVariable long idUnivesity) {
        ApiResponse apiResponse = new ApiResponse();
        try { Universite universite= new ObjectMapper().readValue(u,Universite.class);
            if(logo!=null) {
                String fileName = UUID.randomUUID().toString() + "_" + logo.getOriginalFilename();
                Path targetLocation = Paths.get(directory).resolve(fileName);
                Files.copy(logo.getInputStream(), targetLocation);
                universite.setImage(fileName);
            }
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