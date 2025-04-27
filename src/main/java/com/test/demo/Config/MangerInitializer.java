package com.test.demo.Config;

import com.test.demo.Models.Role;
import com.test.demo.Models.User;
import com.test.demo.Repository.UserRepository;
import com.test.demo.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.test.demo.Security.Jasypt;

@Configuration
public class MangerInitializer {

    @Bean
    public CommandLineRunner addManager(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        PasswordEncoder passwordEncoder,
                                        Jasypt jasypt) {
        return args -> {
            if (userRepository.findByUsername("manager") == null) {

                Role managerRole = roleRepository.findByRolename("MANAGER");

                if (managerRole != null) {

                    User manager = new User();

                    manager.setUsername("manager");

                    manager.setPassword(passwordEncoder.encode("password123"));

                    String encryptedEmail = jasypt.convertToDatabaseColumn("manager@gmail.com");

                    manager.setEmail(encryptedEmail);

                    manager.setRole(managerRole);

                    userRepository.save(manager);

                    System.out.println("manager added to DB successfully");
                } else {
                    System.out.println("manger role not found in DB");
                }
            }
        };
    }
}