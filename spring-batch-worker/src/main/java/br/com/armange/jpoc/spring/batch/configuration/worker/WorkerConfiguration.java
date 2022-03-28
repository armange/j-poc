package br.com.armange.jpoc.spring.batch.configuration.worker;

import br.com.armange.jpoc.spring.batch.configuration.ActiveMqAppProperties;
import br.com.armange.jpoc.spring.batch.configuration.AppProperties;
import br.com.armange.jpoc.spring.batch.configuration.RabbitMqAppProperties;
import br.com.armange.jpoc.spring.batch.configuration.annotation.OnActiveMQ;
import br.com.armange.jpoc.spring.batch.configuration.annotation.OnRabbitMQ;
import br.com.armange.jpoc.spring.batch.configuration.condition.ActiveMQCondition;
import br.com.armange.jpoc.spring.batch.configuration.condition.RabbitMQCondition;
import br.com.armange.jpoc.spring.batch.util.thread.ContextThreadHolder;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@EnableIntegration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:application-rabbitmq.properties")
@PropertySource("classpath:application-activemq.properties")
public class WorkerConfiguration {

    @Bean
    public RabbitMQCondition rabbitMQCondition() {
        return new RabbitMQCondition();
    }

    @Bean
    public ActiveMQCondition activeMQCondition() {
        return new ActiveMQCondition();
    }


    @Bean
    public AppProperties appProperties() {
        return new AppProperties();
    }

    @Bean
    @OnActiveMQ
    public ActiveMqAppProperties activeMqAppProperties() {
        return new ActiveMqAppProperties();
    }

    @Bean
    @OnRabbitMQ
    public RabbitMqAppProperties rabbitMqAppProperties() {
        return new RabbitMqAppProperties();
    }

    @Bean
    @OnRabbitMQ
    public javax.jms.ConnectionFactory rmqConnectionFactory(final RabbitMqAppProperties appProperties) {
        final RMQConnectionFactory connectionFactory = new RMQConnectionFactory();

        connectionFactory.setUsername(appProperties.getRabbitMqUser());
        connectionFactory.setPassword(appProperties.getRabbitMqPassword());
        connectionFactory.setHost(appProperties.getRabbitMqHost());
        connectionFactory.setPort(appProperties.getRabbitMqPort());

        return connectionFactory;
    }

    @Bean
    @OnActiveMQ
    public javax.jms.ConnectionFactory activeMqConnectionFactory(final ActiveMqAppProperties appProperties) {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(appProperties.getActiveMqUrl());
        connectionFactory.setTrustedPackages(List.of("br.com.armange",
                "org.springframework.batch",
                "java.util",
                "java.lang"));
        connectionFactory.setUserName(appProperties.getActiveMqUser());
        connectionFactory.setPassword(appProperties.getActiveMqPassword());

        return connectionFactory;
    }

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(
            final javax.jms.ConnectionFactory connectionFactory,
            final DirectChannel requests) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("requests"))
                .channel(requests)
                .get();
    }

    @Bean
    public DirectChannel replies() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow(
            final javax.jms.ConnectionFactory connectionFactory,
            final DirectChannel replies) {
        return IntegrationFlows
                .from(replies)
                .handle(Jms.outboundAdapter(connectionFactory).destination("replies"))
                .get();
    }

    @Bean
    public ItemProcessor<Integer, Integer> itemProcessor() {
        return item -> {
            log.info("Processing item {}.", item);

            return item;
        };
    }

    @Bean
    public ItemWriter<Integer> itemWriter() {
        return items -> {
            for (final Integer item : items) {
                log.info("Writing item {}.", item);

                if (item == -1) {
                    ContextThreadHolder.setHold(false);
                }
            }
        };
    }

    @Bean
    public IntegrationFlow workerIntegrationFlow(
            final RemoteChunkingWorkerBuilder<Integer, Integer> remoteChunkingWorkerBuilder,
            final ItemProcessor<Integer, Integer> itemProcessor,
            final ItemWriter<Integer> itemWriter,
            final DirectChannel requests,
            final DirectChannel replies) {
        return remoteChunkingWorkerBuilder
                .itemProcessor(itemProcessor)
                .itemWriter(itemWriter)
                .inputChannel(requests)
                .outputChannel(replies)
                .build();
    }

}
