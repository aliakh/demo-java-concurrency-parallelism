package demo.part13_completable_future.part1;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// complete future exceptionally, cause the Throwable
public class CompleteExceptionally extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException());
        logger.info("is completed exceptionally: " + future.isCompletedExceptionally());
        future.get();
    }
}
