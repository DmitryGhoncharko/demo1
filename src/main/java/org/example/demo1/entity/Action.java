package org.example.demo1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Action {
    private int id;
    private String info;
    private String image;
}
