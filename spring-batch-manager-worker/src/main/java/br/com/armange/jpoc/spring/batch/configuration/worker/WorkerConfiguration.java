package br.com.armange.jpoc.spring.batch.configuration.worker;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
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
    public ActiveMQConnectionFactory connectionFactory(
            @Value("${broker.activemq.url}")
            final String brokerUrl) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setTrustedPackages(List.of("br.com.armange",
                "org.springframework.batch",
                "java.util",
                "java.lang"));
        return connectionFactory;
    }

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(final ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("requests"))
                .channel(requests())
                .get();
    }

    @Bean
    public DirectChannel replies() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow(final ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(replies())
                .handle(Jms.outboundAdapter(connectionFactory).destination("replies"))
                .get();
    }

    @Bean
    public ItemProcessor<Integer, Integer> itemProcessor() {
        return item -> {
            log.info("processing item {}.", item);
            return item;
        };
    }

    @Bean
    public ItemWriter<Integer> itemWriter() {
        return items -> {
            for (Integer item : items) {
                log.info("writing item {}.", item);
            }
        };
    }

    @Bean
    public IntegrationFlow workerIntegrationFlow(
            final RemoteChunkingWorkerBuilder<Integer, Integer> remoteChunkingWorkerBuilder) {
        return remoteChunkingWorkerBuilder
                .itemProcessor(itemProcessor())
                .itemWriter(itemWriter())
                .inputChannel(requests())
                .outputChannel(replies())
                .build();
    }

}
