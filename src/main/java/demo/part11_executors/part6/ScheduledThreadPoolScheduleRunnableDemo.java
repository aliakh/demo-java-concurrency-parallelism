package demo.part11_executors.part6;

import demo.common.Demo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolScheduleRunnableDemo extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

        Runnable runnable = () -> logger.info("finished");

        logger.info("started");
        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.schedule(runnable, 3000, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(1000);

        long remainingDelay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
        logger.info("remaining delay: {} millisecond(s)", remainingDelay);

        logger.info("result: {}", scheduledFuture.get());

        shutdown(scheduledThreadPoolExecutor);
    }
}
