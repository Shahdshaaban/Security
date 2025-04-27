package com.test.demo.Models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "role_id")
    private Long role_id;

    @Column(name = "rolename", nullable = false, unique = true)
    private String rolename;


}
