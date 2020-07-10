package demo.part13_completable_future.part1;

import demo.common.Demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// cancel future, cause CancellationException
public class Cancel extends Demo1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.cancel(false);
        logger.info("is cancelled: " + future.isCancelled());
        future.get();
    }
}
