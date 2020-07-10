package demo.part13_completable_future.part3;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// accept result in Consumer after finishing future
public class ThenAccept extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture.supplyAsync(() -> sleepAndGet("value"))
                .thenAccept(s -> logger.info("consumed: " + s))
                .get();
    }
}
