package demo.part13_completable_future.part6;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// apply result in Function after finishing any future
public class ApplyToEither extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "parallel1"))
                .applyToEither(CompletableFuture.supplyAsync(() -> sleepAndGet(2, "parallel2")),
                        String::toUpperCase);
        logger.info("result: " + future.get());
    }
}
