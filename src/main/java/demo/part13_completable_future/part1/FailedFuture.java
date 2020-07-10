package demo.part13_completable_future.part1;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// create already failed future
public class FailedFuture extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException());
        logger.info("is completed exceptionally: " + future.isCompletedExceptionally());
        logger.info("result: " + future.get());
    }
}
