package demo.part11_executors.part3;

import demo.common.Demo2;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceScheduleAtFixedRateDemo extends Demo2 {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        LocalTime start = LocalTime.now();

        Runnable runnable = () -> logger.info("duration from start: {} second(s)", Duration.between(start, LocalTime.now()).getSeconds());
        int initialDelay = 3;
        int period = 1;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS);

        Runnable canceller = () -> {
            scheduledFuture.cancel(true);
            scheduledExecutorService.shutdown();
        };
        scheduledExecutorService.schedule(canceller, 10, TimeUnit.SECONDS);
    }
}
