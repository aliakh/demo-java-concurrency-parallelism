package demo.part13_completable_future.part1;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// handle any result during success or failure of previous stage
public class Handle extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = handle(CompletableFuture.completedFuture("Success"));
        logger.info("result: " + future1.get());

        CompletableFuture<String> future2 = handle(CompletableFuture.failedFuture(new RuntimeException("Failure")));
        logger.info("result: " + future2.get());
    }

    private static CompletableFuture<String> handle(CompletableFuture<String> future) {
        return future.handle((result, e) -> result != null ? result : "Process exception: " + e.getMessage());
    }
}
