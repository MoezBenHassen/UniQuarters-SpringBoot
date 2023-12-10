package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.PasswordResetToken;
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

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse> requestPasswordReset(@PathVariable String email) {
        User user = null;
        ApiResponse apiResponse = new ApiResponse();
        try {
            user = userService.findUserByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setResponse(HttpStatus.NOT_FOUND,"Cet utilisateur n'existe pas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
        service.createPasswordResetToken(user);
        apiResponse.setResponse(HttpStatus.OK,"Le lien de reinitialisation à étè envoyé a votre email");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/reset/{token}/{password}")
    public ResponseEntity<ApiResponse> resetPassword(@PathVariable String token, @PathVariable String password){
        ApiResponse apiResponse = new ApiResponse();
        try{
            service.resetPassword(token, password);
        }catch (Exception e){
            e.printStackTrace();
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, "Une erreur est survenue lors de la reinitialisation de votre mot de passe");
            return ResponseEntity.badRequest().body(apiResponse);
        }
        apiResponse.setResponse(HttpStatus.OK,"Mot de passe réinitialisé avec succées");
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/getRequest/{token}")
    public ResponseEntity<ApiResponse> getRequest(@PathVariable String token) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            PasswordResetToken p = service.getResetToken(token);
            if (p == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "Request not found");
            } else {
                apiResponse.setResponse(HttpStatus.OK, "Request retrieved");
                apiResponse.addData("request", p);
            }
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

}
