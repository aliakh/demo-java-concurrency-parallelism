package demo.part11_executors.part4;

import demo.common.Demo2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorUnboundedQueueDemo extends Demo2 {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        System.out.println(threadPoolExecutor.getQueue().remainingCapacity());

        threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
        threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
        threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
        threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
        threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

        threadPoolExecutor.shutdown();
    }
}
