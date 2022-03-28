package br.com.armange.jpoc.spring.batch;

import br.com.armange.jpoc.spring.batch.configuration.manager.ManagerConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagerApp {

    public static void main(final String[] args) {
        final AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ManagerConfiguration.class);

        final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        final Job job = (Job) context.getBean("remoteChunkingJob");

        log.info("Starting the batch job");

        try {
            final JobExecution execution = jobLauncher.run(job, new JobParameters());

            log.info("Job Status: {}.", execution.getStatus());
            log.info("Job completed");
        } catch (final Exception e) {
            e.printStackTrace();
            log.info("Job failed");
            System.exit(125);
        } finally {
            System.exit(0);
        }
    }
}
