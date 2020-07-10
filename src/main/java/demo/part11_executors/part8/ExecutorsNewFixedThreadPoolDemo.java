package demo.part11_executors.part8;

import demo.common.Demo2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorsNewFixedThreadPoolDemo extends Demo2 {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(() -> sleep(1));
        }

        LocalDateTime start = LocalDateTime.now();
        threadPoolExecutor.shutdown();

        for (int i = 0; i < 10; i++) {
            sleep(1);

            logThreadPoolInformation(threadPoolExecutor);

            if (threadPoolExecutor.isTerminated()) {
                logThreadPoolInformation(threadPoolExecutor);
                break;
            }
        }

        LocalDateTime finish = LocalDateTime.now();
        logger.info("finished: {} millisecond(s)", Duration.between(start, finish).toMillis());
    }

    private static void logThreadPoolInformation(ThreadPoolExecutor threadPoolExecutor) {
        logger.info("tasks count (all/completed) {}/{}, queue (size/capacity) {}/{}, thread pool size (active/current/maximum): {}/{}/{}",
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getQueue().size(),
                threadPoolExecutor.getQueue().remainingCapacity(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getLargestPoolSize()
        );
    }
}
