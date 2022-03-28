package br.com.armange.jpoc.spring.batch.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class AppProperties {

    @Value("${br.com.armange.jpoc.spring.batch.context.timeout:60000}")
    private Long contextTimeout;

    @Value("${br.com.armange.jpoc.spring.batch.context.hold-time:1000}")
    private Long contextHoldTime;
}
