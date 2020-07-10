package demo.part11_executors.part9;

import demo.common.Demo2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorCompletionServicePollTimeoutDemo extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        List<Callable<String>> callables = Arrays.asList(
                () -> sleepAndGet(2, "Bravo"),
                () -> sleepAndGet(1, "Alpha"),
                () -> sleepAndGet(3, "Charlie")
        );

        for (Callable<String> callable : callables) {
            completionService.submit(callable);
        }

        for (int i = 0; i < callables.size(); i++) {
            logDuration("future " + (i + 1),
                    () -> {
                        Future<String> future;
                        do {
                            future = completionService.poll(1, TimeUnit.SECONDS);
                            if (future != null) {
                                logger.info("result: {}", future.get());
                            } else {
                                logger.info("no result yet");
                            }
                        } while (future == null);
                    }
            );
        }

        executorService.shutdown();
    }
}
