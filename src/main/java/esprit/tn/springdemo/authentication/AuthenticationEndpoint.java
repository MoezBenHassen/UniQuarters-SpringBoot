package esprit.tn.springdemo.authentication;

import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.User;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationEndpoint {

    private final AuthenticationService service;
    private final UserRepo userRepo;

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
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        String token;
        try {
            token = service.refreshToken(refreshToken);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/emailAlreadyExists/{email}")
    public String emailExists(@PathVariable String email){
        User u = userRepo.findByEmail(email).orElse(null);
        return u == null ? null : u.getEmail();
    }


}