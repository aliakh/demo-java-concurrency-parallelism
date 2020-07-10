package demo.part12_future;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureCancelDemo2 extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(FutureCancelDemo2.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            sleep(1);
            return "Bravo";
        });

        sleep(2);

        logger.info("cancel: " + future.cancel(true));
        logger.info("is cancelled: " + future.isCancelled());
        logger.info("result: " + future.get());

        shutdown(executorService);
    }
}
