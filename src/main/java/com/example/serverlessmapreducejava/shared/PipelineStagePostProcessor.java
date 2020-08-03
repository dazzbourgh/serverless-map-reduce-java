package com.example.serverlessmapreducejava.shared;

import com.example.serverlessmapreducejava.shared.gcp.domain.PubSubEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class PipelineStagePostProcessor implements BeanPostProcessor {
    private Set<String> beans;
    private Map<String, Class<?>> strategyToEventTypeMap;

    private final ApplicationContext context;
    private final Map<String, InputStrategy> inputStrategies;
    private final Map<String, PipelineTerminalOperation> terminalOperations;

    @PostConstruct
    private void init() {
        // TODO: maybe better have commands that incorporate both?
        beans = Set.of(context.getBeanNamesForAnnotation(PipelineStage.class));
        strategyToEventTypeMap = Map.of(
                "pubSubStrategy", PubSubEvent.class,
                "sqsEventStrategy", LinkedHashMap.class
        );
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beans.contains(beanName)) {
            var pipelineTerminalStage = context.findAnnotationOnBean(beanName, PipelineStage.class);
            var input = pipelineTerminalStage.input();
            var pipelineStageOutput = pipelineTerminalStage.output();

            var inputOption = input.inputOption();
            var inputType = input.type();
            var outputOption = pipelineStageOutput.value();
            return (Function<PubSubEvent, Object>) eventObject -> {
                log.info("Received event: {}", eventObject);
                Optional.ofNullable(inputStrategies.get(inputOption.getName()))
                        .map(toBusinessObjects(inputOption, inputType, eventObject))
                        .orElseGet(Stream::empty)
                        .map(extractedData -> ((Function) bean).apply(extractedData))
                        .forEach(terminalOperations.get(outputOption.getName())::accept);
                // TODO: intentionally returns null as the value is never used, but need to see if there is a better approach
                // Can't return Consumer<PubSub>, since the initial type is Function<Animal, Classification>,
                // and can't use Consumer<Animal> as initial type since need to return mapped value of Classification
                return null;
            };
        } else {
            return bean;
        }
    }

    private Function<InputStrategy, Stream<?>> toBusinessObjects(
            PipelineStage.InputOption inputOption,
            Class<?> inputType,
            Object eventObject) {
        return strategy -> strategy.extract(
                strategyToEventTypeMap.get(inputOption.getName())
                        .cast(eventObject),
                inputType);
    }
}
