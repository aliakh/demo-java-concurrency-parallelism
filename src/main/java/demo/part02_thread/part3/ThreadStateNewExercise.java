package demo.part02_thread.part3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shows Thread part3 NEW.
 */
public class ThreadStateNewExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStateNewExercise.class);

    @Override
    public void run() {
        logger.info("run...");
    }

    public static void main(String[] args) {
        Thread thread = new ThreadStateNewExercise();

        logger.info("before start(): part3={}", thread.getState());
        thread.start();
    }
}