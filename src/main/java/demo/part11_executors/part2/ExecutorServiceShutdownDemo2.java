package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceShutdownDemo2 extends Demo2 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        logger.info("is shutdown: {}", executorService.isShutdown());
        executorService.shutdown();
        logger.info("is shutdown: {}", executorService.isShutdown());

        executorService.submit(() -> "Alpha"); // java.util.concurrent.RejectedExecutionException
    }
}
