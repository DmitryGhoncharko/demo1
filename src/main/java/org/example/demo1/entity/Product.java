package org.example.demo1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
@Builder
public class Product {
    private int id;
    private String name;
    private String date;
    private String image;
}
