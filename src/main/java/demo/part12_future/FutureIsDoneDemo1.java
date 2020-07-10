package demo.part12_future;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureIsDoneDemo1 extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(FutureIsDoneDemo1.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            sleep(3);
            return "Alpha";
        });

        while (!future.isDone()) {
            logger.info("waiting...");
            sleep(1);
        }
        logger.info("result: {}", future.get());

        shutdown(executorService);
    }
}
