package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceAwaitTerminationDemo extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> sleepAndGet(1, "Alpha"));
        executorService.submit(() -> sleepAndGet(1, "Bravo"));
        executorService.submit(() -> sleepAndGet(1, "Charlie"));

        executorService.shutdown();

        logDuration(
                "awaitTermination",
                () -> executorService.awaitTermination(60, TimeUnit.SECONDS)
        );
    }
}
