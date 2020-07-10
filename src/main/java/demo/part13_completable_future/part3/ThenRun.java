package demo.part13_completable_future.part3;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// run Runnable after finishing future
public class ThenRun extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture.supplyAsync(() -> sleepAndGet("value"))
                .thenRun(() -> logger.info("run in Runnable"))
                .get();
    }
}
