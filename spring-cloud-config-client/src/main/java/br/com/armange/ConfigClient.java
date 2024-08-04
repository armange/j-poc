package br.com.armange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RefreshScope
@SpringBootApplication
public class ConfigClient {

    @Value("${example.config-one}")
    private String configOne;

    @Value("${example.config-two:two-default}")
    private String configTwo;

    @Value("${example.config-three}")
    private String configThree;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        System.out.println(configOne);
        System.out.println(configTwo);
        System.out.println(configThree);
        System.out.println(String.join(",", environment.getActiveProfiles()));
    }

    public static void main(String[] arguments) {
        SpringApplication.run(ConfigClient.class, arguments);
    }
}
