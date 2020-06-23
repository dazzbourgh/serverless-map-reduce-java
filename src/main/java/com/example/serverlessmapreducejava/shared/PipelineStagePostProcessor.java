package com.example.serverlessmapreducejava.shared;

import com.example.serverlessmapreducejava.shared.gcp.domain.PubSubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PipelineStagePostProcessor implements BeanPostProcessor {
    private Set<String> beans;
    private final ApplicationContext context;
    private final Map<String, InputStrategy> inputStrategies;
    private final Map<String, PipelineTerminalOperation> terminalOperations;

    @PostConstruct
    private void init() {
        beans = Set.of(context.getBeanNamesForAnnotation(PipelineStage.class));
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
            // TODO: change generic parameters to be generic and not Pub/Sub specific
            return new Function<PubSubEvent, Object>() {
                @Override
                public Object apply(PubSubEvent pubSubEvent) {
                    Optional.ofNullable(inputStrategies.get(inputOption.getName()))
                            .map(strategy -> strategy.extract(pubSubEvent, inputType))
                            .map(extractedData -> ((Function) bean).apply(extractedData))
                            .ifPresent(terminalOperations.get(outputOption.getName())::accept);
                    // TODO: intentionally returns null as the value is never used, but need to see if there is a better approach
                    // Can't return Consumer<PubSub>, since the initial type is Function<Animal, Classification>,
                    // and can't use Consumer<Animal> as initial type since need to return mapped value of Classification
                    return null;
                }
            };
        } else {
            return bean;
        }
    }
}
