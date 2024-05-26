package org.example.demo1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Support {
    private int id;
    private User user;
    private String message;
    private String response;
    private boolean status;
}
