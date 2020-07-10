package demo.part11_executors.part7;

import demo.common.Demo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinPoolSubmitDemo extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        ForkJoinTask<Long> forkJoinTask = forkJoinPool.submit(task);
        Long count = forkJoinTask.get();
        System.out.println("count: " + count); // 9592

        forkJoinPool.shutdown();
    }
}

