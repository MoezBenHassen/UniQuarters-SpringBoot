package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IPasswordReset;
import esprit.tn.springdemo.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-reset")
@AllArgsConstructor
public class PasswordResetController {
    private final IPasswordReset service;
    private final IUserService userService;

    @PostMapping("/{email}")
    public ResponseEntity<String> requestPasswordReset(@PathVariable String email) {
        User user = null;
        try {
            user = userService.findUserByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cet utilisateur n'existe pas");
        }
        service.createPasswordResetToken(user);
        return ResponseEntity.status(HttpStatus.OK).body("Le lien de reinitialisation à étè envoyé a votre email");
    }

    @PutMapping("/reset/{token}/{password}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @PathVariable String password){
        try{
            service.resetPassword(token, password);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Une erreur est survenue lors de la reinitialisation de votre mot de passe");
        }
        return ResponseEntity.ok().body("Mot de passe réinitialisé avec succées");
    }

}
