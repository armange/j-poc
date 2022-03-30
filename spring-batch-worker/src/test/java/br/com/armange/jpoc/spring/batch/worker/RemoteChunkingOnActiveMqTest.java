package br.com.armange.jpoc.spring.batch.worker;

import br.com.armange.jpoc.spring.batch.worker.configuration.AppProperties;
import br.com.armange.jpoc.spring.batch.worker.configuration.WorkerConfiguration;
import br.com.armange.jpoc.spring.batch.worker.util.thread.ContextThreadHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@Testcontainers
class RemoteChunkingOnActiveMqTest {

    @Container
    public GenericContainer<?> actimeMqContainer = new GenericContainer<>(DockerImageName
            .parse("vromero/activemq-artemis:latest-alpine"))
            .withNetworkMode("host")
            .withEnv("ARTEMIS_USERNAME","user-amq")
            .withEnv("ARTEMIS_PASSWORD","password-amq")
            .waitingFor(Wait.forLogMessage(".+Artemis Console available at http.+", 1));

    @Container
    public GenericContainer<?> springBatchManagerContainer = new GenericContainer<>(DockerImageName
            .parse("diegoarmangecosta/poc:spring-batch-manager-1.0.0-snapshot"))
            .withNetworkMode("host")
            .withEnv("SPRING_PROFILES_ACTIVE","activemq")
            .withEnv("BR_COM_ARMANGE_JPOC_SPRING_BATCH_BROKER_ACTIVEMQ_URL","tcp://127.0.0.1:61616")
            .withEnv("BR_COM_ARMANGE_JPOC_SPRING_BATCH_BROKER_ACTIVEMQ_USER","user-amq")
            .withEnv("BR_COM_ARMANGE_JPOC_SPRING_BATCH_BROKER_ACTIVEMQ_PASSWORD","password-amq")
            .waitingFor(Wait.forLogMessage(".+Manager started successfully.+", 1));

    @Test
    @SuppressWarnings("unchecked")
    void shouldDoRemoteChunking() throws InterruptedException {
        final AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(WorkerConfiguration.class);
        final AppProperties appProperties = context.getBean(AppProperties.class);
        final List<Integer> memory = (List<Integer>) BeanFactoryAnnotationUtils
                .qualifiedBeanOfType(context.getBeanFactory(), List.class, "memory");

        ContextThreadHolder.hold(
                appProperties.getContextTimeout(),
                appProperties.getContextHoldTime(), () -> {
                    context.stop();
                    context.close();
                });

        Thread.sleep(3500);
        assertThat(memory, contains(1, 2, 3, 4, 5, 6, 7, 8, 9, -1));
    }
}
