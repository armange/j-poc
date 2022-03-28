package br.com.armange.jpoc.spring.batch.configuration.worker;

import br.com.armange.jpoc.spring.batch.configuration.AppProperties;
import br.com.armange.jpoc.spring.batch.util.thread.ContextThreadHolder;
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
public class WorkerConfiguration {

    @Bean
    public ActiveMQConnectionFactory connectionFactory(final AppProperties appProperties) {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(appProperties.getBrokerUrl());
        connectionFactory.setTrustedPackages(List.of("br.com.armange",
                "org.springframework.batch",
                "java.util",
                "java.lang"));
        connectionFactory.setUserName("user-amq");
        connectionFactory.setPassword("password-amq");

        return connectionFactory;
    }

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(
            final ActiveMQConnectionFactory connectionFactory,
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
            final ActiveMQConnectionFactory connectionFactory,
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
