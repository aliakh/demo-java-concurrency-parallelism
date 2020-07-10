package demo.part10_synchronizers.part1;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoresDemo1 extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(SemaphoresDemo1.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Semaphore semaphore = new Semaphore(2);
        Runnable runnable = () -> {
            try {
                logger.info("try to acquire...");
                if (semaphore.tryAcquire(2, TimeUnit.SECONDS)) {
                    logger.info("acquired");
                    Thread.sleep(2000);
                } else {
                    logger.info("don't acquired");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                logger.info("released");
                semaphore.release();
            }
        };

        for (int i = 0; i < 10; i++) {
            executorService.execute(runnable);
        }

        shutdown(executorService);
    }
}
