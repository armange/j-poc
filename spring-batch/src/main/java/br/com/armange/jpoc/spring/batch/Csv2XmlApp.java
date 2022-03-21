package br.com.armange.jpoc.spring.batch;

import br.com.armange.jpoc.spring.batch.configuration.CentralConfiguration;
import br.com.armange.jpoc.spring.batch.configuration.Csv2XmlBatchConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Csv2XmlApp {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(CentralConfiguration.class);
        context.register(Csv2XmlBatchConfig.class);
        context.refresh();

        final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        final Job job = (Job) context.getBean("csv2Xml");

        System.out.println("Starting the batch job");

        try {
            final JobExecution execution = jobLauncher.run(job, new JobParameters());

            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (final Exception e) {
            e.printStackTrace();
            System.out.println("Job failed");
        }
    }
}
