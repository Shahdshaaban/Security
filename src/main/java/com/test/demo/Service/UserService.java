package com.test.demo.Service;

import com.test.demo.Models.Role;
import com.test.demo.Models.User;
import com.test.demo.Repository.RoleRepository;
import com.test.demo.Repository.UserRepository;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${jasypt.encryptor.password}")
    private String encryptionPassword;

    // Method to find user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Method to save user with encrypted email
    public void save(User user) {
        try {
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                String encryptedEmail = encryptEmail(user.getEmail());
                user.setEmail(encryptedEmail);
            }
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to find role by name
    public Role findRoleByName(String roleName) {
        return roleRepository.findByRolename(roleName);
    }

    // Method to encrypt email using Jasypt
    public String encryptEmail(String email) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptionPassword);
        return encryptor.encrypt(email);
    }

    // Method to decrypt email using Jasypt
    public String decryptEmail(String encryptedEmail) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptionPassword);
        return encryptor.decrypt(encryptedEmail);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            try {
                if (user.getEmail() != null) {
                    String decryptedEmail = decryptEmail(user.getEmail());
                    user.setEmail(decryptedEmail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

//    public List<User> getAllEmployees() {
//        List<User> employees = userRepository.findAll()
//                .stream()
//                .filter(user -> user.getRole().getRolename().equals("EMPLOYEE"))
//                .collect(Collectors.toList());
//
//        for (User employee : employees) {
//            try {
//                if (employee.getEmail() != null) {
//                    String decryptedEmail = decryptEmail(employee.getEmail());
//                    employee.setEmail(decryptedEmail);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return employees;
//    }

}
