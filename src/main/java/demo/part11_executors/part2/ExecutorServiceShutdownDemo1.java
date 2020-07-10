package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceShutdownDemo1 extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> sleepAndGet(1, "Alpha"));
        executorService.submit(() -> sleepAndGet(1, "Bravo"));
        executorService.submit(() -> sleepAndGet(1, "Charlie"));

        logger.info("is shutdown: {}", executorService.isShutdown());

        logDuration(
                "shutdown",
                () -> executorService.shutdown()
        );

        logger.info("is shutdown: {}", executorService.isShutdown());
    }
}
