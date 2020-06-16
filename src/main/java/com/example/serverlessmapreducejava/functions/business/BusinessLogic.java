package com.example.serverlessmapreducejava.functions.business;

import com.example.serverlessmapreducejava.domain.Animal;
import com.example.serverlessmapreducejava.domain.Classification;
import com.example.serverlessmapreducejava.utils.queue.QueueSender;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class BusinessLogic {
    private final Consumer<Animal> consume;

    public BusinessLogic(QueueSender queueSender, @Value("${topic}") String topic) {
        this.consume = animal -> queueSender.send(animal, topic);
    }

    @Bean
    public Consumer<String> read() {
        return pathString -> {
            Path path = Paths.get(URI.create(pathString));
            try (Stream<String> lines = getLines(path)) {
                lines.map(toAnimal()).forEach(consume);
            }
        };
    }

    @SneakyThrows
    private Stream<String> getLines(Path path) {
        return Files.lines(path);
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
