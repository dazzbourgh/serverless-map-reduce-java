package com.example.serverlessmapreducejava.specific.function;

import com.example.serverlessmapreducejava.shared.PipelineStage;
import com.example.serverlessmapreducejava.shared.PipelineStage.PipelineStageInput;
import com.example.serverlessmapreducejava.shared.PipelineStage.PipelineStageOutput;
import com.example.serverlessmapreducejava.specific.domain.Animal;
import com.example.serverlessmapreducejava.specific.domain.Classification;
import com.example.serverlessmapreducejava.intermediate.utils.QueueSender;
import com.example.serverlessmapreducejava.intermediate.utils.StorageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.example.serverlessmapreducejava.shared.PipelineStage.InputOption.PUB_SUB_EVENT;
import static com.example.serverlessmapreducejava.shared.PipelineStage.OutputOption.BIG_QUERY;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
public class BusinessLogic {
    private final Function<Animal, CompletableFuture<Void>> consume;
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
                var future = lines
                        .skip(1)
                        .map(toAnimal())
                        .map(consume)
                        .collect(collectingAndThen(toList(),
                                futures -> allOf(futures.toArray(new CompletableFuture[0]))));
                await(future);
            }
        };
    }

    @Bean
    @PipelineStage(
            input = @PipelineStageInput(
                    type = Animal.class,
                    inputOption = PUB_SUB_EVENT),
            output = @PipelineStageOutput(BIG_QUERY))
    public Function<Animal, Classification> classify() {
        // long running operation
        return animal -> {
            boolean favorite = !"snake".equalsIgnoreCase(animal.getType());
            return new Classification(animal.getType(), animal.isWild(), favorite);
        };
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
