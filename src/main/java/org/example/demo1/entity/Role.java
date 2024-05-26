package org.example.demo1.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Role {
    private int roleId;
    private String roleName;
}
