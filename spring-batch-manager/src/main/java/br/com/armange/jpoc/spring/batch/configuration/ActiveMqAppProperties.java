package br.com.armange.jpoc.spring.batch.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class ActiveMqAppProperties {

    @Value("${br.com.armange.jpoc.spring.batch.broker.activemq.url:tcp://localhost:61616}")
    private String activeMqUrl;

    @Value("${br.com.armange.jpoc.spring.batch.broker.activemq.user:user-amq}")
    private String activeMqUser;

    @Value("${br.com.armange.jpoc.spring.batch.broker.activemq.password:password-amq}")
    private String activeMqPassword;
}
