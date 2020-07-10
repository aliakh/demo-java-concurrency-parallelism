package demo.part02_thread.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts a thread that extends the Thread class from an anonymous class.
 */
public class ExtendThreadAnonymousClassExercise {

    private static final Logger logger = LoggerFactory.getLogger(ExtendThreadAnonymousClassExercise.class);

    public static void main(String[] args) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                logger.info("run...");
            }
        };

        thread.start();
    }
}
