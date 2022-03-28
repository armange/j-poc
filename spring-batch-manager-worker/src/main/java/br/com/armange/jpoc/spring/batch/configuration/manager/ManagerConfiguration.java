package br.com.armange.jpoc.spring.batch.configuration.manager;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@EnableIntegration
@PropertySource("classpath:csv2xml.properties")
public class ManagerConfiguration {

    @Bean
    public ActiveMQConnectionFactory connectionFactory(
            @Value("${broker.activemq.url}")
            final String brokerUrl) {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setTrustAllPackages(true);

        return connectionFactory;
    }

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(requests())
                .handle(Jms.outboundAdapter(connectionFactory).destination("requests"))
                .get();
    }

    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("replies"))
                .channel(replies())
                .get();
    }

    @Bean
    public ListItemReader<Integer> itemReader() {
        return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    @Bean
    public TaskletStep managerStep(
            final RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory) {
        return managerStepBuilderFactory.get("managerStep")
                .<Integer, Integer>chunk(3)
                .reader(itemReader())
                .outputChannel(requests())
                .inputChannel(replies())
                .build();
    }

    @Bean
    public Job remoteChunkingJob(
            final RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory,
            final JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("remoteChunkingJob")
                .start(managerStep(managerStepBuilderFactory))
                .build();
    }

}
