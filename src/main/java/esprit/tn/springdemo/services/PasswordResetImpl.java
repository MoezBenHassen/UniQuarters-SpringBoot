package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.PasswordResetToken;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.repositories.PasswordResetRepo;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetImpl implements IPasswordReset{
    private final PasswordResetRepo passwordResetRepo;
    private final IUserService userService;
    private final EmailService emailService;
    @Override
    public void createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken p = PasswordResetToken.builder().valid(true).token(token).user(user).build();
        try {
            String body = "Votre lien de reinitialisation: http://localhost:4200/password-reset/"+token;
            emailService.sendEmail(user.getEmail(), "Reinitialisation de votre mot de passe UniQuarters", body);
        } catch (MessagingException e){e.printStackTrace();}
        passwordResetRepo.save(p);
    }

    @Override
    public void disableToken(PasswordResetToken token) {
        token.setValid(false);
        passwordResetRepo.save(token);
    }

    @Override
    public void resetPassword(String token, String password) {
        PasswordResetToken p = passwordResetRepo.findByToken(token);
        p.getUser().setPassword(password);
        userService.updatePassword(p.getUser());
        disableToken(p);
    }
}
