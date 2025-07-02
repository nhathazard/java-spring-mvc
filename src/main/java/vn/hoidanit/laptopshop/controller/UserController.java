package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

@RestController
public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @RequestMapping("")
    public String getHomePage() {
        return "home.html";
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.POST)
    public String createUserPage(@RequestBody User user) {
        System.out.println("nhat" + user);
        this.userService.handleSaveUser(user);
        return "admin";
    }
}
