package br.com.armange.jpoc.spring.batch.manager.configuration.annotation;

import br.com.armange.jpoc.spring.batch.manager.configuration.condition.ActiveMQCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ActiveMQCondition.class)
public @interface OnActiveMQ {
}
