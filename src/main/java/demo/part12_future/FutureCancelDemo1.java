package demo.part12_future;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureCancelDemo1 extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(FutureCancelDemo1.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            sleep(1);
            return "Alpha";
        });

        logger.info("cancel: " + future.cancel(true));
        logger.info("is cancelled: " + future.isCancelled());
        logger.info("result: " + future.get()); // java.util.concurrent.CancellationException

        shutdown(executorService); // never happens
    }
}
