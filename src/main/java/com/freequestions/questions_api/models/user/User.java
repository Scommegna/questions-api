package com.freequestions.questions_api.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    protected User() {

    }

    public User(String email, String password, Role role, String name, String lastName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public void changePassword(String newHashedPassword) {
        this.password = newHashedPassword;
    }

    public void updateName(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }
}
