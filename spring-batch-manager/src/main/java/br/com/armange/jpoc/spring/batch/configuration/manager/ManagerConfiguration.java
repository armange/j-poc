package br.com.armange.jpoc.spring.batch.configuration.manager;

import br.com.armange.jpoc.spring.batch.configuration.AppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@EnableIntegration
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class ManagerConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;

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
    public IntegrationFlow outboundFlow(
            final ActiveMQConnectionFactory connectionFactory,
            final DirectChannel requests) {
        return IntegrationFlows
                .from(requests)
                .handle(Jms.outboundAdapter(connectionFactory).destination("requests"))
                .get();
    }

    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(final ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("replies"))
                .channel(replies())
                .get();
    }

    @Bean
    public ListItemReader<Integer> itemReader() {
        return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, -1));
    }

    @Bean
    public TaskletStep managerStep(
            final ListItemReader<Integer> itemReader,
            final DirectChannel requests,
            final QueueChannel replies) {
        return this.managerStepBuilderFactory.get("managerStep")
                .<Integer, Integer>chunk(3)
                .reader(itemReader)
                .outputChannel(requests)
                .inputChannel(replies)
                .build();
    }

    @Bean
    public Job remoteChunkingJob(final TaskletStep managerStep) {
        return this.jobBuilderFactory.get("remoteChunkingJob")
                .start(managerStep)
                .build();
    }

}
