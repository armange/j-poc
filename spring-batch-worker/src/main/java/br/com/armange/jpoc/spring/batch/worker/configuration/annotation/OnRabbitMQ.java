package br.com.armange.jpoc.spring.batch.worker.configuration.annotation;

import br.com.armange.jpoc.spring.batch.worker.configuration.condition.RabbitMQCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(RabbitMQCondition.class)
public @interface OnRabbitMQ {
}
