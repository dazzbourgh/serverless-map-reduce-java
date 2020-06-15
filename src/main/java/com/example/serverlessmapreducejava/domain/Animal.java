package com.example.serverlessmapreducejava.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Animal {
    private String type;
    private boolean wild;
}
