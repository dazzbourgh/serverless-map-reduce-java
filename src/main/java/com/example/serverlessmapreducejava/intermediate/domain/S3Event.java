package com.example.serverlessmapreducejava.intermediate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class S3Event {
    @JsonProperty("Records")
    private List<S3Record> records;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class S3Record {
        private S3 s3;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class S3 {
        private S3Bucket bucket;
        private S3Object object;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class S3Bucket {
        private String name;
        private String arn;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class S3Object {
        private String key;
    }
}
