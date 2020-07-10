package demo.part06_liveness;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class DeadlockDemo extends Demo2 {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            logger.info("waiting for lock 1...");
            synchronized (lock1) {
                logger.info("holding lock 1...");
                sleep(1);

                logger.info("waiting for lock 2...");
                synchronized (lock2) {
                    logger.info("holding locks 1, 2...");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            logger.info("waiting for lock 2...");
            synchronized (lock2) {
                logger.info("holding lock 2...");
                sleep(1);

                logger.info("waiting for lock 1...");
                synchronized (lock1) {
                    logger.info("holding locks 2, 1...");
                }
            }
        });

        thread1.start();
        thread2.start();

        sleep(2);

        logger.info("thread 1 id: {} ", thread1.getId());
        logger.info("thread 2 id: {} ", thread2.getId());

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        logger.info("deadlocked treads ids: {} ", Arrays.toString(threadMXBean.findDeadlockedThreads()));
    }
}