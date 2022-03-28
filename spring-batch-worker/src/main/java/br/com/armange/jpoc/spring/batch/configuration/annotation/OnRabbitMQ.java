package br.com.armange.jpoc.spring.batch.configuration.annotation;

import br.com.armange.jpoc.spring.batch.configuration.condition.RabbitMQCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(RabbitMQCondition.class)
public @interface OnRabbitMQ {
}
