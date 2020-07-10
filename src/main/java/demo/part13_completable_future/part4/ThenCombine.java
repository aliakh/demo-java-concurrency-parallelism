package demo.part13_completable_future.part4;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// run two futures parallelly and then combine them
public class ThenCombine extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("parallel1"))
                .thenCombine(CompletableFuture.supplyAsync(() -> sleepAndGet("parallel2")),
                        (s1, s2) -> s1 + " " + s2);
        logger.info("result: " + future.get());
    }
}
