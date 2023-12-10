package esprit.tn.springdemo.controllers;

import esprit.tn.springdemo.entities.Role;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.responses.ApiResponse;
import esprit.tn.springdemo.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final IUserService iUserService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getUsers() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<User> Users = iUserService.getAllUsers();
            apiResponse.setResponse(HttpStatus.OK, "Users retrieved");
            apiResponse.addData("users", Users);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addUser(@RequestBody User user) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            User addedUser = iUserService.addUser(user);
            //throw new RuntimeException("Test exception");
            apiResponse.setResponse(HttpStatus.CREATED, "User added");
            apiResponse.addData("user", addedUser);
        } catch (Exception ex) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @PutMapping("")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody User user) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            User foundUser = iUserService.retrieveUser(user.getId());
            if (foundUser == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "User not found");
            } else {
                User updatedUser = iUserService.updateUser(user);
                apiResponse.setResponse(HttpStatus.OK, "User updated");
                apiResponse.addData("user", updatedUser);
            }

        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable long idUser) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            User User = iUserService.retrieveUser(idUser);
            if (User == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "User not found");
            } else {
                apiResponse.setResponse(HttpStatus.OK, "User retrieved");
                apiResponse.addData("user", User);
            }
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long idUser) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            User User = iUserService.retrieveUser(idUser);
            if (User == null) {
                apiResponse.setResponse(HttpStatus.NOT_FOUND, "User not found");
                return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
            } else {
                iUserService.removeUser(idUser);
                apiResponse.setResponse(HttpStatus.OK, "User deleted");
            }
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable Role role) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<User> Users = iUserService.getUsersByRole(role);
            apiResponse.setResponse(HttpStatus.OK, "Users retrieved");
            apiResponse.addData("users", Users);
        } catch (Exception e) {
            apiResponse.setResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(apiResponse, apiResponse._getHttpStatus());
    }

}
