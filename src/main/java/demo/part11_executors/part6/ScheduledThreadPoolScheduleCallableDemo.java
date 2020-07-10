package demo.part11_executors.part6;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolScheduleCallableDemo extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledThreadPoolScheduleCallableDemo.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

        Callable<String> callable = () -> {
            logger.info("finished");
            return "Alpha";
        };

        logger.info("started");
        ScheduledFuture<String> scheduledFuture = scheduledThreadPoolExecutor.schedule(callable, 3000, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(1000);

        long remainingDelay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
        logger.info("remaining delay: {}  millisecond(s)", remainingDelay);

        logger.info("result: {}", scheduledFuture.get());

        shutdown(scheduledThreadPoolExecutor);
    }
}
