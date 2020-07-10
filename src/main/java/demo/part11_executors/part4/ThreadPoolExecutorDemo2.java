package demo.part11_executors.part4;

import demo.common.Demo2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo2 extends Demo2 {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(2));

        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(() -> sleep(1));
        }

        for (int i = 0; i < 10; i++) {
            sleep(1);

            logThreadPoolSize(threadPoolExecutor);

            if (threadPoolExecutor.isTerminated()) {
                logThreadPoolSize(threadPoolExecutor);
                break;
            }
        }

        threadPoolExecutor.shutdown();
    }

    private static void logThreadPoolSize(ThreadPoolExecutor threadPoolExecutor) {
        logger.info("thread pool size (active/current/maximum): {}/{}/{}",
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getLargestPoolSize()
        );
    }
}

