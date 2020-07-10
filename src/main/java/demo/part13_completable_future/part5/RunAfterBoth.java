package demo.part13_completable_future.part5;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// run Runnable after finishing both futures
public class RunAfterBoth extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture.supplyAsync(() -> sleepAndGet(1, "parallel1"))
                .runAfterBoth(CompletableFuture.supplyAsync(() -> sleepAndGet(2, "parallel2")),
                        () -> logger.info("finished both"))
                .get();
    }
}

