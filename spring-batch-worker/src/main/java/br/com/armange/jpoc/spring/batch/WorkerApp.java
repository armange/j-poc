package br.com.armange.jpoc.spring.batch;

import br.com.armange.jpoc.spring.batch.configuration.AppProperties;
import br.com.armange.jpoc.spring.batch.configuration.worker.WorkerConfiguration;
import br.com.armange.jpoc.spring.batch.util.thread.ContextThreadHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    }
}
