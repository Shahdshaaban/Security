package com.test.demo.Controller;


import com.test.demo.Models.User;
import com.test.demo.Repository.UserRepository;
import com.test.demo.Response.LoginResponse;
import com.test.demo.Security.Jasypt;
import com.test.demo.Service.RefreshTokenService;
import com.test.demo.Service.UserService;
import com.test.demo.DTO.AuthRequest;
import com.test.demo.Models.Role;
import com.test.demo.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Jasypt jasypt;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        if (userService.findByUsername(authRequest.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }

        User newUser = new User();
        newUser.setUsername(authRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        newUser.setEmail(authRequest.getEmail());

        Role employeeRole = userService.findRoleByName("EMPLOYEE");
        newUser.setRole(employeeRole);

        userService.save(newUser);

        return "User registered successfully!";
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        User user = userService.findByUsername(authRequest.getUsername());

        if (user == null || !passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT and Refresh token
        String jwtToken = jwtUtil.generateToken(user.getUsername(), user.getRole().getRolename());
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        // Create and populate the LoginResponse object
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(jwtToken);
        loginResponse.setRefreshToken(refreshToken);

        // Return the response as a ResponseEntity
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/home")
    public String home(){
        return "welcome to server";
    }



    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUserInfo(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwt);

            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            String decryptedEmail = jasypt.convertToEntityAttribute(user.getEmail());

            String roleName = user.getRole().getRolename();

            Map<String, String> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", decryptedEmail);
            response.put("role", roleName);

            if ("EMPLOYEE".equals(roleName)) {
                return ResponseEntity.ok(response);

            } else if ("MANAGER".equals(roleName)) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or decryption error");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        try {

            var tokenEntity = refreshTokenService.findByToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));

            // check in DB if token exist or not

            refreshTokenService.verifyExpiration(tokenEntity); // verify if token has expired or not

            User user = tokenEntity.getUser();


            String newJwtToken = jwtUtil.generateToken(user.getUsername(), user.getRole().getRolename());


            String newRefreshToken = refreshTokenService.createRefreshToken(user).getToken();


            LoginResponse response = new LoginResponse();

            response.setJwtToken(newJwtToken);

            response.setRefreshToken(newRefreshToken);


            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

}
