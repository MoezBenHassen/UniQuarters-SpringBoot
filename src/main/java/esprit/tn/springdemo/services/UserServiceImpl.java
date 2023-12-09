package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Role;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {return (userRepo.findAll().isEmpty() ? null : userRepo.findAll());}

    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User retrieveUser(long idUser) {
        return userRepo.findById(idUser).orElse(null);
    }

    @Override
    public void removeUser(long idUser) {
        userRepo.deleteById(idUser);
    }

    @Override
    public User findUserByEmail(String email) {
        User u = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found with the email: " + email));
        return u;
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }

    @Override
    public void updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }


}
