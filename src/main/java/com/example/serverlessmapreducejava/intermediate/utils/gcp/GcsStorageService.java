package com.example.serverlessmapreducejava.intermediate.utils.gcp;

import com.example.serverlessmapreducejava.intermediate.utils.StorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class GcsStorageService implements StorageService {
    private final Storage storage;

    public GcsStorageService(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Stream<String> get(String bucket, String key) {
        BlobId blobId = BlobId.of(bucket, key);
        Blob blob = storage.get(blobId);
        return new BufferedReader(Channels.newReader(blob.reader(), StandardCharsets.UTF_8)).lines();
    }
}
