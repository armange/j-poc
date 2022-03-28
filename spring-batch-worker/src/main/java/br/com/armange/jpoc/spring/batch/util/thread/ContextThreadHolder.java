package br.com.armange.jpoc.spring.batch.util.thread;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextThreadHolder {
    private static final long MAX_HOLD_TIME = 1000L;
    private static final long MIN_HOLD_TIME = 100L;

    @Setter
    private static Boolean hold = Boolean.TRUE;

    public static synchronized void hold(
            final long timeout, final long holdTime, final Runnable timeoutTrigger) {
        final Thread holder = new Thread(() -> {
            final long localTimeout = System.currentTimeMillis() + timeout;

            while (Boolean.TRUE.equals(ContextThreadHolder.hold)) {
                if (System.currentTimeMillis() > localTimeout) {
                    log.info("Timeout reached.");

                    break;
                }

                try {
                    final long sleepTime = holdTime <= MAX_HOLD_TIME && holdTime >= MIN_HOLD_TIME
                            ? holdTime : MAX_HOLD_TIME;

                    log.info("Sleeping {} millis...", sleepTime);
                    Thread.sleep(sleepTime);
                } catch (final InterruptedException e) {
                    ContextThreadHolder.hold = Boolean.FALSE;
                    log.info("Thread interrupted.");
                    Thread.currentThread().interrupt();
                }
            }
            log.info("The thread holding is finished..");
            Optional.ofNullable(timeoutTrigger).ifPresent(Runnable::run);
        });

        holder.start();
    }
}

