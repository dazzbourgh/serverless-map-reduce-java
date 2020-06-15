package com.example.serverlessmapreducejava.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Classification {
    private Animal animal;
    private boolean favorite;
}
