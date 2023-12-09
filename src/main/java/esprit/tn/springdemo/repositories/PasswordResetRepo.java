package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepo extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
