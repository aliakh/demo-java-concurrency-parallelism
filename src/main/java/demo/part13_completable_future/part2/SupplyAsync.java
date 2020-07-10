package demo.part13_completable_future.part2;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// complete future after returning value from Supplier asynchronously
public class SupplyAsync extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));
        logger.info("result: " + future.get());
    }
}
