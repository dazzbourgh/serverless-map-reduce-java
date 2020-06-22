package com.example.serverlessmapreducejava.specific.utils.gcp;

import com.example.serverlessmapreducejava.specific.utils.StorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class GcsStorageService implements StorageService {
    private static Pattern pattern = Pattern.compile("gs://(?<bucket>.+?)/(?<file>.+)");
    private final Storage storage;

    public GcsStorageService(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Stream<String> get(String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            BlobId blobId = BlobId.of(matcher.group("bucket"), matcher.group("file"));
            Blob blob = storage.get(blobId);
            return new BufferedReader(Channels.newReader(blob.reader(), StandardCharsets.UTF_8)).lines();
        } else {
            throw new IllegalArgumentException(path);
        }
    }
}
