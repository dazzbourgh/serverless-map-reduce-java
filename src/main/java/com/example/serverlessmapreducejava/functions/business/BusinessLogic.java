package com.example.serverlessmapreducejava.functions.business;

import com.example.serverlessmapreducejava.domain.Animal;
import com.example.serverlessmapreducejava.domain.Classification;
import com.example.serverlessmapreducejava.utils.database.ClassificationDao;
import com.example.serverlessmapreducejava.utils.queue.QueueSender;
import com.example.serverlessmapreducejava.utils.storage.StorageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
public class BusinessLogic {
    private final Function<Animal, CompletableFuture<Void>> consume;
    private final StorageService storageService;
    private final ClassificationDao classificationDao;

    public BusinessLogic(QueueSender queueSender,
                         @Value("${topic}") String topic,
                         StorageService storageService,
                         ClassificationDao classificationDao) {
        this.storageService = storageService;
        this.classificationDao = classificationDao;
        this.consume = animal -> queueSender.send(animal, topic);
    }

    @Bean
    public Consumer<String> read() {
        return pathString -> {
            try (Stream<String> lines = storageService.get(pathString)) {
                var future = lines
                        .map(toAnimal())
                        .map(consume)
                        .collect(collectingAndThen(toList(),
                                futures -> allOf(futures.toArray(new CompletableFuture[0]))));
                await(future);
            }
        };
    }

    @Bean
    public Function<Animal, Classification> classify() {
        // long running operation
        return animal -> {
            boolean favorite = "snake".equalsIgnoreCase(animal.getType());
            return new Classification(animal, favorite);
        };
    }

    @Bean
    public Consumer<Classification> store() {
        return classificationDao::save;
    }

    private Function<String, Animal> toAnimal() {
        return line -> {
            String[] words = line.split(",");
            return new Animal(words[0], Boolean.getBoolean(words[1]));
        };
    }

    @SneakyThrows
    private void await(CompletableFuture<Void> completableFuture) {
        completableFuture.get();
    }
}
