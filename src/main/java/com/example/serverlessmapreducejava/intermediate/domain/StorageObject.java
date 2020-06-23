package com.example.serverlessmapreducejava.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageObject {
    private String bucket;
    private String key;
}
