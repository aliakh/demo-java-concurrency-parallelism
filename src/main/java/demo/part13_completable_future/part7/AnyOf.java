package demo.part13_completable_future.part7;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// wait for any of futures to finish
public class AnyOf extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "parallel1"));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "parallel2"));
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> sleepAndGet(3, "parallel3"));

        CompletableFuture<Object> future = CompletableFuture.anyOf(future2, future1, future3);
        logger.info("result: " + future.get());
    }
}
