package br.com.armange.jpoc.spring.batch;

import br.com.armange.jpoc.spring.batch.configuration.CentralConfiguration;
import br.com.armange.jpoc.spring.batch.configuration.DtoMultiThread2DtoConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Dto2DtoApp {
    public static void main(String[] args) {
        // Spring Java config
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CentralConfiguration.class);
        context.register(DtoMultiThread2DtoConfig.class);
        context.refresh();

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("dto2Dto-job");
        System.out.println("Starting the batch job");
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Job failed");
        }
    }
}