package demo.part11_executors.part4;

import demo.common.Demo2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorExtendDemo extends Demo2 {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ExtendedThreadPoolExecutor();

        threadPoolExecutor.submit(() -> sleep(1));

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
                threadPoolExecutor.getCompletedTaskCount()
        );
    }

    private static class ExtendedThreadPoolExecutor extends ThreadPoolExecutor {

        private ExtendedThreadPoolExecutor() {
            super(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            logger.info("before task execution: thread {}, task {}", t, r);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            logger.info("after task execution: task {}, exception {}", r, t);
        }

        @Override
        protected void terminated() {
            super.terminated();
            logger.info("is terminated");
        }
    }
}

