package com.test.demo.Controller;

import com.test.demo.Models.User;
import com.test.demo.Service.UserService;
import com.test.demo.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/get-manager")
    public String printManagerInfo(@RequestHeader("Authorization") String token) throws Exception {

        String jwt = token.replace("Bearer ", "");

        String username = jwtUtil.extractUsername(jwt);

        User user = userService.findByUsername(username);

        String decryptedEmail = userService.decryptEmail(user.getEmail());

        return "welcome to employee page your username is: " + username +
                " and your decrypted email is: " + decryptedEmail;
    }
}