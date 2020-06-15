package com.example.serverlessmapreducejava.domain.gcp;

import lombok.Data;

@Data
public class GcsEvent {
    private JsonPayload jsonPayload;
}
