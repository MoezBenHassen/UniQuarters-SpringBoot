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
    public void run(String...args) throws Exception {
        User test = userService.findUserByEmail("admin@admin");
        if(test == null){
        User admin = User.builder().email("admin@admin").password("admin").role(Role.ADMIN).build();
        userService.addUser(admin);
        }
    }

}
