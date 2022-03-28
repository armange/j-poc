package br.com.armange.jpoc.spring.batch.configuration.manager;

import br.com.armange.jpoc.spring.batch.configuration.ActiveMqAppProperties;
import br.com.armange.jpoc.spring.batch.configuration.RabbitMqAppProperties;
import br.com.armange.jpoc.spring.batch.configuration.annotation.OnActiveMQ;
import br.com.armange.jpoc.spring.batch.configuration.annotation.OnRabbitMQ;
import br.com.armange.jpoc.spring.batch.configuration.condition.ActiveMQCondition;
import br.com.armange.jpoc.spring.batch.configuration.condition.RabbitMQCondition;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
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
@PropertySource("classpath:application-rabbitmq.properties")
@PropertySource("classpath:application-activemq.properties")
@RequiredArgsConstructor
public class ManagerConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;

    @Bean
    public RabbitMQCondition rabbitMQCondition() {
        return new RabbitMQCondition();
    }

    @Bean
    public ActiveMQCondition activeMQCondition() {
        return new ActiveMQCondition();
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
    public IntegrationFlow outboundFlow(
            final javax.jms.ConnectionFactory connectionFactory,
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
    public IntegrationFlow inboundFlow(final javax.jms.ConnectionFactory connectionFactory) {
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
