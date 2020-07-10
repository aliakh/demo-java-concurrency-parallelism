package demo.common;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Demo2 extends Demo1 {

    // https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ExecutorService.html
    protected static void shutdown(ExecutorService executorService) {
        logger.debug("executor service: shutdown started");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                List<Runnable> skippedTasks = executorService.shutdownNow();
                logger.error("count of tasks never commenced execution: {}", skippedTasks.size());
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("executor service didn't terminate");
                }
            }
        } catch (InterruptedException e) {
            logger.error("executor service is interrupted", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.debug("executor service: shutdown finished");
    }
}
