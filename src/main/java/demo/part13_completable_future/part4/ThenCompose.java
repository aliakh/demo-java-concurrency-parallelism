package demo.part13_completable_future.part4;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenCompose extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("sequential1"))
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> sleepAndGet(s + " sequential2")));
        logger.info("result: " + future.get());
    }
}
