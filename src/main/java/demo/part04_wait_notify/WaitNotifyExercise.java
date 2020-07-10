package demo.part04_wait_notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitNotifyExercise {

    private static final Logger logger = LoggerFactory.getLogger(WaitNotifyExercise.class);

    public static void main(String[] args) throws InterruptedException {
        Object monitor = new Object();

        new Thread(() -> {
            logger.info("thread 1: waits synchronized");
            synchronized (monitor) {
                try {
                    logger.info("thread 1: before wait()");
                    monitor.wait();
                    logger.info("thread 1: after wait()");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            logger.info("thread 1: leaves synchronized");
        }).start();

        Thread.sleep(1000);

        logger.info("thread 2: waits synchronized");
        synchronized (monitor) {
            logger.info("thread 2: before notify()");
            monitor.notify();
            logger.info("thread 2: after notify()");
        }
        logger.info("thread 2: leaves synchronized");
    }
}