package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Role;
import esprit.tn.springdemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
