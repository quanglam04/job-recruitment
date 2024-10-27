package vn.trinhlam.jobhunter.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.trinhlam.jobhunter.domain.User;
import vn.trinhlam.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("user/create")
    @PostMapping("user")
    public User createNewUser(@RequestBody User user) {

        // User user = new User();
        // user.setEmail("trinhquanglam2k4@gmail.com");
        // user.setName("trinhlam");
        // user.setPassword("123123");
        User newUser = this.userService.createUser(user);
        return newUser;
    }

    @DeleteMapping("user/{id}")
    public String deleteUser(@PathVariable("id") long id) {

        // User user = new User();
        // user.setEmail("trinhquanglam2k4@gmail.com");
        // user.setName("trinhlam");
        // user.setPassword("123123");
        this.userService.delete(id);
        return "delete";
    }
}
