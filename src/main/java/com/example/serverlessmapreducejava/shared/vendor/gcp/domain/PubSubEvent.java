package com.example.serverlessmapreducejava.shared.vendor.gcp.domain;

import lombok.Data;

import java.util.Map;

@Data
public class PubSubEvent {
    private String data;
    private Map<String, String> attributes;
    private String messageId;
    private String publishTime;
}
