package vn.hoidanit.laptopshop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("")
    public String getHomePage() {
        return "home.html";
    }

    @PostMapping("/admin/user/create")
    public User postMethodName(@RequestBody  User user) {
        System.out.println("user" + user);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName(user.getRole().getName()));
        return this.userService.handleSaveUser(user);
    }

    @PostMapping("/admin/user/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path path = Paths.get("src/main/resources/images/avatar/" + filename);
    Files.copy(file.getInputStream(), path);
    return ResponseEntity.ok(filename);
}

    @RequestMapping(value = "/admin/user", params = "email")
    public List<User> getUserByEmail(@RequestParam String email) {
        return this.userService.getUserByEmail(email);
    }
}
