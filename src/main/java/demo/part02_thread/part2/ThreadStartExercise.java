package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses Thread.start() method.
 */
public class ThreadStartExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStartExercise.class);

    @Override
    public void run() {
        logger.info("run...");
    }

    public static void main(String[] args) {
        Thread thread = new ThreadStartExercise();

        logger.info("before run()");
        thread.run();

        logger.info("before start()");
        thread.start();
    }
}