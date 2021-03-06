package br.com.armange.jpoc.spring.batch.worker.configuration.annotation;

import br.com.armange.jpoc.spring.batch.worker.configuration.condition.ActiveMQCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ActiveMQCondition.class)
public @interface OnActiveMQ {
}
