package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutorServiceInvokeAnyDemo2 extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> sleepAndGet(2, "Bravo"),
                () -> sleepAndGet(1, "Alpha"),
                () -> sleepAndGet(3, "Charlie")
        );

        String result = executorService.invokeAny(callables, 500, TimeUnit.MILLISECONDS); // java.util.concurrent.TimeoutException
        logger.info("result: {}", result);

        executorService.shutdown(); // never happens
    }
}
