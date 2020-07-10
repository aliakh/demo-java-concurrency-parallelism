package demo.part02_thread.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts a thread from a Runnable from a lambda.
 */
public class ImplementRunnableLambdaExercise {

    private static final Logger logger = LoggerFactory.getLogger(ImplementRunnableLambdaExercise.class);

    public static void main(String[] args) {
        Runnable runnable = () -> logger.info("run...");

        Thread thread = new Thread(runnable);
        thread.start();
    }
}