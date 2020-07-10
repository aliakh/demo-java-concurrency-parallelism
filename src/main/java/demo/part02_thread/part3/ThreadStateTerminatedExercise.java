package demo.part02_thread.part3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shows Thread part3 TERMINATED.
 */
public class ThreadStateTerminatedExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStateTerminatedExercise.class);

    @Override
    public void run() {
        logger.info("run...");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new ThreadStateTerminatedExercise();
        thread.start();

        thread.join();
        logger.info("after join(): part3={}", thread.getState());
    }
}