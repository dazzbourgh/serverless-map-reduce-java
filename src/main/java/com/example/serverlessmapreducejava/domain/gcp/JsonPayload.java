package com.example.serverlessmapreducejava.domain.gcp;

import lombok.Data;

@Data
public class JsonPayload {
    private String bucket;
    private String name;
    private String contentType;
    private String mediaLink;
    private String selfLink;
}
