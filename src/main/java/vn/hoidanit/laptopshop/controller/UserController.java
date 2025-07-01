package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.service.UserService;

@RestController
public class UserController {
    private UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("")
    public String getHomePage() {
        return "home.html";
    }

    @RequestMapping("/admin/user")
    public String getUserPage() {
        return "admin";
    }
}
