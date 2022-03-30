package br.com.armange.jpoc.spring.batch.manager.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class RabbitMqAppProperties {

    @Value("${br.com.armange.jpoc.spring.batch.broker.rabbitmq.host:localhost}")
    private String rabbitMqHost;

    @Value("${br.com.armange.jpoc.spring.batch.broker.rabbitmq.port:5672}")
    private Integer rabbitMqPort;

    @Value("${br.com.armange.jpoc.spring.batch.broker.rabbitmq.user:user}")
    private String rabbitMqUser;

    @Value("${br.com.armange.jpoc.spring.batch.broker.rabbitmq.password:password}")
    private String rabbitMqPassword;

}
