package com.test.demo.Controller;

import com.test.demo.Models.User;
import com.test.demo.Service.UserService;
import com.test.demo.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/get-employee")
    public ResponseEntity<String> printEmployeeInfo(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing or invalid.");
            }

            String jwt = token.replace("Bearer ", "");

            String username = jwtUtil.extractUsername(jwt);

            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            String decryptedEmail = userService.decryptEmail(user.getEmail());

            return ResponseEntity.ok(" username: " + username +
                    "  email : " + decryptedEmail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
