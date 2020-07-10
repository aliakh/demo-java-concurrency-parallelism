package demo.part11_executors.part3;

import demo.common.Demo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceScheduleRunnableDemo extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        Runnable runnable = () -> logger.info("finished");

        logger.info("started");
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(runnable, 3000, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(1000);

        long remainingDelay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
        logger.info("remaining delay: {} millisecond(s)", remainingDelay);

        logger.info("result: {}", scheduledFuture.get());

        shutdown(scheduledExecutorService);
    }
}
