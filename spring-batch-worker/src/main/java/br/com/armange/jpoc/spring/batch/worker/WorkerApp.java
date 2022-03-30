package br.com.armange.jpoc.spring.batch.worker;

import br.com.armange.jpoc.spring.batch.worker.configuration.AppProperties;
import br.com.armange.jpoc.spring.batch.worker.configuration.WorkerConfiguration;
import br.com.armange.jpoc.spring.batch.worker.util.thread.ContextThreadHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerApp {

    public static void main(final String[] args) {
        final AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(WorkerConfiguration.class);
        final AppProperties appProperties = context.getBean(AppProperties.class);

        ContextThreadHolder.hold(
                appProperties.getContextTimeout(),
                appProperties.getContextHoldTime(), () -> {
            context.stop();
            context.close();
        });

        log.info("Manager started successfully.");
    }
}
