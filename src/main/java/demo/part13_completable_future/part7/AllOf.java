package demo.part13_completable_future.part7;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// wait for all of futures to finish
public class AllOf extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "parallel1"));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "parallel2"));
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> sleepAndGet(3, "parallel3"));

        CompletableFuture<Void> future = CompletableFuture.allOf(future2, future1, future3);
        future.get();

        String result = Stream.of(future1, future2, future3)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(", "));

        logger.info("result: " + result);
    }
}
