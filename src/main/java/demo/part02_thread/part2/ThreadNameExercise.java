package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses Thread name.
 */
public class ThreadNameExercise implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ThreadNameExercise.class);

    @Override
    public void run() {
        logger.info("run...");
    }

    public static void main(String[] args) {
        Runnable runnable = new ThreadNameExercise();

        Thread thread = new Thread(runnable, "runner");
        logger.info("name={}", thread.getName());

        thread.start();
    }
}