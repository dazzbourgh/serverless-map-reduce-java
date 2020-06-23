package com.example.serverlessmapreducejava.intermediate.utils.aws;

import com.example.serverlessmapreducejava.intermediate.utils.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "provider", havingValue = "aws")
@AllArgsConstructor
public class S3StorageService implements StorageService {
    private final S3Client s3;

    @Override
    public Stream<String> get(String bucket, String key) {
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        var response = s3.getObject(getObjectRequest);
        return new BufferedReader(new InputStreamReader(response)).lines();
    }
}
