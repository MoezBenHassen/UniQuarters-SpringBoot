package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Role;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    User addUser(User user);
    User updateUser(User u);
    User retrieveUser(long idUser);
    void removeUser(long idUser);
    User findUserByEmail(String email) throws Exception;
    List<User> getUsersByRole(Role role);
}
