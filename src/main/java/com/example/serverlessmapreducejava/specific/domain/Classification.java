package com.example.serverlessmapreducejava.specific.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Classification {
    private String type;
    private boolean wild;
    private boolean favorite;
}
