package demo.part12_future;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureIsDoneDemo2 extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(FutureIsDoneDemo2.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future1 = executorService.submit(() -> {
            sleep(2);
            return "Alpha";
        });
        logger.info("result: {}", future1.get(3, TimeUnit.SECONDS));

        Future<String> future2 = executorService.submit(() -> {
            sleep(2);
            return "Bravo";
        });
        logger.info("result: {}", future2.get(1, TimeUnit.SECONDS)); // java.util.concurrent.TimeoutException

        shutdown(executorService); // never happens
    }
}
