package esprit.tn.springdemo.authentication;

import esprit.tn.springdemo.config.JwtService;
import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.repositories.EtudiantRepo;
import esprit.tn.springdemo.repositories.UserRepo;
import esprit.tn.springdemo.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationEndpoint {

    private final AuthenticationService service;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final EtudiantRepo etudiantRepo;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody Etudiant etudiant) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.addData("added user", service.register(etudiant));
            apiResponse.setResponse(HttpStatus.CREATED, "Etudiant registered");
        } catch (Exception ex) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse auth;
        try {
            auth = service.authenticate(request);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody String refreshToken) {
        String token;
        ApiResponse apiResponse = new ApiResponse();
        try {
            token = service.refreshToken(refreshToken);
            apiResponse.addData("newToken",token);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/emailAlreadyExists/{email}")
    public String emailExists(@PathVariable String email){
        User u = userRepo.findByEmail(email).orElse(null);
        return u == null ? null : u.getEmail();
    }

    @GetMapping("/userByToken/{token}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable String token) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String email = jwtService.extractUsername(token);
            User user = userRepo.findByEmail(email).orElse(null);
            if (user == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "User not found");
            } else {
                apiResponse.setResponse(HttpStatus.OK, "User retrieved");
                apiResponse.addData("user", user);
            }
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/etudiantByToken/{token}")
    public ResponseEntity<ApiResponse> getEtudiant(@PathVariable String token) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String email = jwtService.extractUsername(token);
            Etudiant etudiant = etudiantRepo.findEtudiantByUserEmail(email).orElse(null);
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


}