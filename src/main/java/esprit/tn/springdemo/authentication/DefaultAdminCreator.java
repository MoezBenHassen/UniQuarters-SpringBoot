package esprit.tn.springdemo.authentication;

import esprit.tn.springdemo.entities.Role;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.repositories.UserRepo;
import esprit.tn.springdemo.services.IUserService;
import esprit.tn.springdemo.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminCreator implements CommandLineRunner {

    @Autowired
    IUserService userService;

    @Override
    public void run(String... args) throws Exception {
        try {
            User admin = userService.findUserByEmail("admin@admin");
            if (admin == null) {
                throw new Exception("Admin not found");
            }
        } catch (Exception e) {
            User admin = new User();
            admin.setEmail("admin@admin");
            admin.setPassword("admin");
            admin.setRole(Role.ADMIN);
            userService.addUser(admin);
        }

    }
}
