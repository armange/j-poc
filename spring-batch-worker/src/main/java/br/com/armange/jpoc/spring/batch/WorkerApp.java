package br.com.armange.jpoc.spring.batch;

import br.com.armange.jpoc.spring.batch.configuration.AppProperties;
import br.com.armange.jpoc.spring.batch.util.thread.ContextThreadHolder;
import br.com.armange.jpoc.spring.batch.configuration.worker.WorkerConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerApp {

    public static void main(final String[] args) {
        final AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppProperties.class, WorkerConfiguration.class);
        final long timeout = ((long)1000) * 60;

        ContextThreadHolder.hold(timeout, 1000, () -> {
            context.stop();
            context.close();
        });
    }
}
