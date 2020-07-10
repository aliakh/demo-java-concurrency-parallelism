package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceShutdownNowDemo extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> sleepAndGet(1, "Alpha"));
        executorService.submit(() -> sleepAndGet(1, "Bravo"));
        executorService.submit(() -> sleepAndGet(1, "Charlie"));

        logger.info("is terminated: {}", executorService.isTerminated());

        logDuration(
                "shutdownNow",
                () -> {
                    List<Runnable> skippedTasks = executorService.shutdownNow();
                    logger.info("count of tasks never commenced execution: {}", skippedTasks.size());
                }
        );

        logger.info("is terminated: {}", executorService.isTerminated());
    }
}
