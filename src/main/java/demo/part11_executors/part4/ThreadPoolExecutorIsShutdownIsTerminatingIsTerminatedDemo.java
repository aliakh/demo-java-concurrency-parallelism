package demo.part11_executors.part4;

import demo.common.Demo2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorIsShutdownIsTerminatingIsTerminatedDemo extends Demo2 {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1));

        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(() -> sleep(1));
        }

        logger.info("isShutdown={} isTerminating={} isTerminated={}",
                threadPoolExecutor.isShutdown(),
                threadPoolExecutor.isTerminating(),
                threadPoolExecutor.isTerminated()
        );

        threadPoolExecutor.shutdown();

        logger.info("isShutdown={} isTerminating={} isTerminated={}",
                threadPoolExecutor.isShutdown(),
                threadPoolExecutor.isTerminating(),
                threadPoolExecutor.isTerminated()
        );

        for (int i = 0; i < 20; i++) {
            sleep(1);

            logger.info("isShutdown={} isTerminating={} isTerminated={}",
                    threadPoolExecutor.isShutdown(),
                    threadPoolExecutor.isTerminating(),
                    threadPoolExecutor.isTerminated()
            );

            if (threadPoolExecutor.isTerminated())
                break;
        }
    }
}

