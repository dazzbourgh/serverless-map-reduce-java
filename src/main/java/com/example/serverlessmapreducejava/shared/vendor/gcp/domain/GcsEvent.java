package com.example.serverlessmapreducejava.shared.vendor.gcp.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GcsEvent {
    private String bucket;
    private String name;
    private String metageneration;
    private Date timeCreated;
    private Date updated;
}
