package demo.part13_completable_future.part6;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// accept result in Consumer after finishing any future
public class AcceptEither extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture.supplyAsync(() -> sleepAndGet(1, "parallel1"))
                .acceptEither(CompletableFuture.supplyAsync(() -> sleepAndGet(2, "parallel2")),
                        s -> logger.info("consumed one: " + s))
                .get();
    }
}
