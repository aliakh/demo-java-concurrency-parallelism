package demo.part13_completable_future.part2;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// complete future after running Runnable asynchronously
public class RunAsync extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> logger.info(sleepAndGet("value")));
        logger.info("result: " + future.get());
    }
}
