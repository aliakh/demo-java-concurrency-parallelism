package demo.part11_executors.part9;

import demo.common.Demo2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorCompletionServiceDemo extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        List<Future<String>> futures = new ArrayList<>();
        futures.add(completionService.submit(() -> sleepAndGet(2, "Bravo")));
        futures.add(completionService.submit(() -> sleepAndGet(1, "Alpha")));
        futures.add(completionService.submit(() -> sleepAndGet(3, "Charlie")));

        for (int i = 0; i < futures.size(); i++) {
            logger.info("result: {}", completionService.take().get());
        }

        executorService.shutdown();
    }
}
