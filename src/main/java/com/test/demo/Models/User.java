package com.test.demo.Models;

import com.test.demo.Security.Jasypt;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id ;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    @Convert(converter = Jasypt.class)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
