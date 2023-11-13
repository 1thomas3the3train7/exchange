package com.udsu.auth.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String updateEmail;
    private Role[] roles;

    public User(String email, Role[] roles) {
        this.email = email;
        this.roles = roles;
    }
}
