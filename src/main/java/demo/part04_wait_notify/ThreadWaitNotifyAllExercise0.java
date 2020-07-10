package demo.part04_wait_notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadWaitNotifyAllExercise0 {

    private static final Logger logger = LoggerFactory.getLogger(ThreadWaitNotifyAllExercise1.class);

    private static final Object monitor = new Object();

    private static Integer counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            logger.info("before synchronized");
            synchronized (monitor) {
                logger.info("after synchronized");
                for (int i = 0; i < 1_000_000; i++) {
                    counter++;
                }
                logger.info("before notify()");
                monitor.notify();
                logger.info("after notify()");
            }
        });

        thread.start();

        logger.info("before synchronized");
        synchronized (monitor) {
            logger.info("before wait()");
            monitor.wait(1000);
            logger.info("after wait()");
            System.out.println(counter);
        }
    }
}