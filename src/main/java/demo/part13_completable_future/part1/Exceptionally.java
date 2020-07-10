package demo.part13_completable_future.part1;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// process exception during success or failure of previous stage
public class Exceptionally extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = exceptionally(CompletableFuture.completedFuture("Success"));
        logger.info("result: " + future1.get());

        CompletableFuture<String> future2 = exceptionally(CompletableFuture.failedFuture(new RuntimeException("Failure")));
        logger.info("result: " + future2.get());
    }

    private static CompletableFuture<String> exceptionally(CompletableFuture<String> future) {
        return future.exceptionally(e -> "Process exception: " + e.getMessage());
    }
}
