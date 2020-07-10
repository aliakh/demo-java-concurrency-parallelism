package demo.part13_completable_future.part3;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// apply Function after finishing future
public class ThenApply extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        logger.info("result: " + CompletableFuture.supplyAsync(() -> sleepAndGet("value"))
                .thenApply(String::toUpperCase)
                .get());
    }
}
