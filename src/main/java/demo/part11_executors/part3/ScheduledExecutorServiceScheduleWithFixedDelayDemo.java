package demo.part11_executors.part3;

import demo.common.Demo2;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceScheduleWithFixedDelayDemo extends Demo2 {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        LocalTime start = LocalTime.now();

        Runnable runnable = () -> {
            sleep(2);
            logger.info("duration from start: {} second(s)", Duration.between(start, LocalTime.now()).getSeconds());
        };
        int initialDelay = 3;
        int delay = 1;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.SECONDS);

        Runnable canceller = () -> {
            scheduledFuture.cancel(true);
            scheduledExecutorService.shutdown();
        };
        scheduledExecutorService.schedule(canceller, 10, TimeUnit.SECONDS);
    }
}
