package br.com.armange.jpoc.spring.batch.config;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobRunnerConfiguration {

    @Bean
    public JobLauncherTestUtils utils() throws Exception {
        return new JobLauncherTestUtils();
    }
}