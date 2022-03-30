package demo.part02_thread.part3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shows Thread states TIMED_WAITING and BLOCKED.
 */
public class ThreadStatesTimedWaitingBlockedExercise {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStatesTimedWaitingBlockedExercise.class);

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                synchronized (this) {
                    try {
                        logger.info("run...");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();

        Thread.sleep(1000);

        logger.info("thread 1 state={}", thread1.getState());
        logger.info("thread 2 state={}", thread2.getState());
    }
}
