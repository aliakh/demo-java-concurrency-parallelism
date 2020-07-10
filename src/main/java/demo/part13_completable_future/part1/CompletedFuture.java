package demo.part13_completable_future.part1;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// create already completed future
public class CompletedFuture extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");
        logger.info("is done: " + future.isDone());
        logger.info("result: " + future.get());
    }
}
