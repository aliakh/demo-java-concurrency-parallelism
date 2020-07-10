package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExecutorServiceInvokeAllDemo2 extends Demo2 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> sleepAndGet(2, "Bravo"),
                () -> sleepAndGet(1, "Alpha"),
                () -> sleepAndGet(3, "Charlie")
        );

        List<String> results = executorService.invokeAll(callables, 2, TimeUnit.SECONDS)
                .stream()
                .peek(future -> logger.info("is done: {}, is cancelled: {}",
                        future.isDone(),
                        future.isCancelled()))
                .map(future -> {
                    try {
                        return future.get(); // java.util.concurrent.CancellationException
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        logger.info("results: {}", results);

        executorService.shutdown(); // never happens
    }
}
