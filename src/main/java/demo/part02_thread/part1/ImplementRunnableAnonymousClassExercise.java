package demo.part02_thread.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts a thread from a Runnable from an anonymous class.
 */
public class ImplementRunnableAnonymousClassExercise {

    private static final Logger logger = LoggerFactory.getLogger(ImplementRunnableAnonymousClassExercise.class);

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.info("run...");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
