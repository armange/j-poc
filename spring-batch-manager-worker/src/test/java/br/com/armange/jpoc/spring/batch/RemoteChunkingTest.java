package br.com.armange.jpoc.spring.batch;

import br.com.armange.jpoc.spring.batch.config.JobRunnerConfiguration;
import br.com.armange.jpoc.spring.batch.configuration.manager.ManagerConfiguration;
import br.com.armange.jpoc.spring.batch.configuration.worker.WorkerConfiguration;
import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JobRunnerConfiguration.class, ManagerConfiguration.class})
@PropertySource("classpath:application.properties")
class RemoteChunkingTest {

    private static final String BROKER_DATA_DIRECTORY = "target/activemq-data";

    @Value("${broker.activemq.url}")
    private String brokerUrl;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private BrokerService brokerService;

    private AnnotationConfigApplicationContext workerApplicationContext;

    @BeforeEach
    public void setUp() throws Exception {
        this.brokerService = new BrokerService();
        this.brokerService.addConnector(this.brokerUrl);
        this.brokerService.setDataDirectory(BROKER_DATA_DIRECTORY);
        this.brokerService.start();

        this.workerApplicationContext = new AnnotationConfigApplicationContext(WorkerConfiguration.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
        this.workerApplicationContext.stop();
        this.workerApplicationContext.close();
        this.brokerService.stop();
    }

    @Test
    void testRemoteChunkingJob() throws Exception {
        final JobExecution jobExecution = this.jobLauncherTestUtils.launchJob();

        assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
    }
}
