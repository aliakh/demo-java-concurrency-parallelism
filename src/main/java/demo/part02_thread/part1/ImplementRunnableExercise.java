package demo.part02_thread.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts a thread that implements the Runnable interface.
 */
public class ImplementRunnableExercise implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ImplementRunnableExercise.class);

    @Override
    public void run() {
        logger.info("run...");
    }

    public static void main(String[] args) {
        Runnable runnable = new ImplementRunnableExercise();
        Thread thread = new Thread(runnable);

        thread.start();
    }
}