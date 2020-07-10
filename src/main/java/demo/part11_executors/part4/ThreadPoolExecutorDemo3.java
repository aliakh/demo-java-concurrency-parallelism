package demo.part11_executors.part4;

import demo.common.Demo2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo3 extends Demo2 {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(() -> sleep(1));
        }

        for (int i = 0; i < 10; i++) {
            sleep(1);

            logTasksCount(threadPoolExecutor);

            if (threadPoolExecutor.isTerminated()) {
                logTasksCount(threadPoolExecutor);
                break;
            }
        }

        threadPoolExecutor.shutdown();
    }

    private static void logTasksCount(ThreadPoolExecutor threadPoolExecutor) {
        logger.info("tasks count (all/completed): {}/{}",
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount());
    }
}

