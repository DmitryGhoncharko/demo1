package org.example.demo1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
    private int userId;
    private String name;
    private String phone;
    private String cardNumber;
    private String username;
    private String password;
    private Role role;
}
