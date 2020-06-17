package com.example.serverlessmapreducejava.functions.business;

import com.example.serverlessmapreducejava.domain.Animal;
import com.example.serverlessmapreducejava.domain.Classification;
import com.example.serverlessmapreducejava.utils.queue.QueueSender;
import com.example.serverlessmapreducejava.utils.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class BusinessLogic {
    private final Consumer<Animal> consume;
    private final StorageService storageService;

    public BusinessLogic(QueueSender queueSender,
                         @Value("${topic}") String topic,
                         StorageService storageService) {
        this.storageService = storageService;
        this.consume = animal -> queueSender.send(animal, topic);
    }

    @Bean
    public Consumer<String> read() {
        return pathString -> {
            try (Stream<String> lines = storageService.get(pathString)) {
                lines.map(toAnimal()).forEach(consume);
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


    private Function<String, Animal> toAnimal() {
        return line -> {
            String[] words = line.split(",");
            return new Animal(words[0], Boolean.getBoolean(words[1]));
        };
    }
}
