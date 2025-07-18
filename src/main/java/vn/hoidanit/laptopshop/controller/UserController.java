package vn.hoidanit.laptopshop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.LoginRequest;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
    public Optional<User> getUserByEmail(@RequestParam String email) {
        return this.userService.getUserByEmail(email);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println("loginRequest" + loginRequest);
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Gắn Authentication vào SecurityContext → Spring Security ghi nhớ user đang đăng nhập
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kích hoạt session (stateful)
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Đăng nhập thành công");
        response.put("username", authentication.getName());
        return response;
    }
}
