package demo.part11_executors.part7;

import demo.common.Demo2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinPoolExecuteDemo extends Demo2 {

    public static void main(String[] args) {
        ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        forkJoinPool.execute(task);

        do {
            sleep(100);
        } while (!task.isDone());

        System.out.println("count: " + task.getRawResult()); // 9592

        forkJoinPool.shutdown();
    }
}

