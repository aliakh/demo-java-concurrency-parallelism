package demo.part11_executors.part7;

import demo.common.Demo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinPoolExecuteDemo2 extends Demo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        forkJoinPool.execute(task);

        do {
            logger.info("getParallelism={}, getPoolSize={}, getActiveThreadCount={}, getRunningThreadCount={}, isQuiescent={}",
                    forkJoinPool.getParallelism(),
                    forkJoinPool.getPoolSize(),
                    forkJoinPool.getActiveThreadCount(),
                    forkJoinPool.getRunningThreadCount(),
                    forkJoinPool.isQuiescent()
            );
            Thread.sleep(100);
        } while (!task.isDone());

        System.out.println("count: " + task.getRawResult()); // 9592

        forkJoinPool.shutdown();
    }
}

