package demo.part11_executors.part9;

import demo.common.Demo2;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorCompletionServiceSubmitCallableDemo extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        completionService.submit(() -> sleepAndGet(1, "Alpha"));

        logger.info("result: {}", completionService.take().get());

        executorService.shutdown();
    }
}
