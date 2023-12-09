package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.PasswordResetToken;
import esprit.tn.springdemo.entities.User;

public interface IPasswordReset {

    void createPasswordResetToken(User user);
    void disableToken(PasswordResetToken token);
    void resetPassword(String token, String password);
}
