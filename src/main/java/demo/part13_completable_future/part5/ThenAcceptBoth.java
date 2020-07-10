package demo.part13_completable_future.part5;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// accept results in BiConsumer after finishing both futures
public class ThenAcceptBoth extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture.supplyAsync(() -> sleepAndGet(1, "parallel1"))
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> sleepAndGet(2, "parallel2")),
                        (s1, s2) -> logger.info("consumed both: " + s1 + " " + s2))
                .get();
    }
}
