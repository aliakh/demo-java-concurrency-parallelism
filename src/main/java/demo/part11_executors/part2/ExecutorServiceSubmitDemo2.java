package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceSubmitDemo2 extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = () -> System.out.println("run...");
        Future<String> future = executorService.submit(runnable, "Alpha");
        System.out.println("result: " + future.get());

        executorService.shutdown();
    }
}
