package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceSubmitDemo3 extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> "Bravo";
        Future<String> future = executorService.submit(callable);
        System.out.println("result: " + future.get());

        executorService.shutdown();
    }
}
