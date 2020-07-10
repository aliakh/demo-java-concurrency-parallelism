package demo.part02_thread.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts a thread that extends the Thread class.
 */
public class ExtendThreadExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ExtendThreadExercise.class);

    @Override
    public void run() {
        logger.info("run...");
    }

    public static void main(String[] args) {
        Thread thread = new ExtendThreadExercise();

        thread.start();
    }
}
