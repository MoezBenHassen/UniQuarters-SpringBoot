package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.PasswordResetToken;
import esprit.tn.springdemo.entities.User;

import java.util.Optional;

public interface IPasswordReset {

    void createPasswordResetToken(User user);
    void disableToken(PasswordResetToken token);
    void resetPassword(String token, String password);
    PasswordResetToken getResetToken(String token);
}
