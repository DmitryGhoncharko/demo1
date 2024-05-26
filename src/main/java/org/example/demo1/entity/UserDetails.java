package org.example.demo1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
public class UserDetails {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private String gender;
    private String birthdate;
    private User user;
    private String userEmail;
}
