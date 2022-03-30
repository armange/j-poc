package br.com.armange.jpoc.spring.batch.manager.configuration.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;

public class ActiveMQCondition implements Condition {

    @Override
    public boolean matches(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
        final List<String> profiles = List.of(context.getEnvironment().getActiveProfiles());

        return profiles.contains("activemq");
    }
}