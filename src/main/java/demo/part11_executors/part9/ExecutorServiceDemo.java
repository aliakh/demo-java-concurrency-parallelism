package demo.part11_executors.part9;

import demo.common.Demo2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceDemo extends Demo2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Future<String>> futures = new ArrayList<>();
        futures.add(executorService.submit(() -> sleepAndGet(2, "Bravo")));
        futures.add(executorService.submit(() -> sleepAndGet(1, "Alpha")));
        futures.add(executorService.submit(() -> sleepAndGet(3, "Charlie")));

        for (Future<String> future : futures) {
            logger.info("result: {}", future.get());
        }

        executorService.shutdown();
    }
}
