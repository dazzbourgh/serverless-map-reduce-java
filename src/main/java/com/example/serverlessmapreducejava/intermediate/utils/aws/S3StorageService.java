package com.example.serverlessmapreducejava.intermediate.utils.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.serverlessmapreducejava.intermediate.utils.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "provider", havingValue = "aws")
@AllArgsConstructor
public class S3StorageService implements StorageService {
    private final AmazonS3 s3;

    @Override
    public Stream<String> get(String bucket, String key) {
        S3Object o = s3.getObject(bucket, key);
        S3ObjectInputStream s3ObjectInputStream = o.getObjectContent();
        return new BufferedReader(new InputStreamReader(s3ObjectInputStream)).lines();
    }
}
