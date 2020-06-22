package com.example.serverlessmapreducejava.specific.domain.gcp;

import lombok.Data;

import java.util.Map;

@Data
public class PubSubEvent {
    private String data;
    private Map<String, String> attributes;
    private String messageId;
    private String publishTime;
}
