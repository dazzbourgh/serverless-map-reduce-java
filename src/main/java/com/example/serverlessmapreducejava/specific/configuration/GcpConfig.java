package com.example.serverlessmapreducejava.specific.configuration;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class GcpConfig {
    @Bean
    public Storage storage() {
        return StorageOptions.newBuilder().build().getService();
    }
}
