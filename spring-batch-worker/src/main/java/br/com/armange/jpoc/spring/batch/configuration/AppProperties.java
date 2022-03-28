package br.com.armange.jpoc.spring.batch.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("${br.com.armange.jpoc.spring.batch.broker.activemq.url:tcp://localhost:61616}")
    private String brokerUrl;

    @Value("${br.com.armange.jpoc.spring.batch.context.timeout:60000}")
    private Long contextTimeout;

    @Value("${br.com.armange.jpoc.spring.batch.context.hold-time:1000}")
    private Long contextHoldTime;
}
