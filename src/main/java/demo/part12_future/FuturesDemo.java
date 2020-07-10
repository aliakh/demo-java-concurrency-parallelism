package demo.part12_future;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FuturesDemo extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(FuturesDemo.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<String> future1 = executorService.submit(() -> sleepAndGet(2, "future 1"));
        Future<String> future2 = executorService.submit(() -> sleepAndGet(4, "future 2"));

        LocalTime start = LocalTime.now();
        while (!(future1.isDone() && future2.isDone())) {
            logger.info("waiting...");
            sleep(1);
        }
        logger.info("duration (seconds): {}", Duration.between(start, LocalTime.now()).getSeconds());

        String result1 = future1.get();
        String result2 = future2.get();
        logger.info("result: " + result1 + " " + result2);

        shutdown(executorService);
    }

}
