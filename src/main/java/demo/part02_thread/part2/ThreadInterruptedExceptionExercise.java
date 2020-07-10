package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interrupts a Thread and gets java.lang.InterruptedException.
 */
public class ThreadInterruptedExceptionExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadInterruptedExceptionExercise.class);

    @Override
    public void run() {
        try {
            Thread.sleep(1000); // java.lang.InterruptedException: sleep interrupted
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new ThreadInterruptedExceptionExercise();
        thread.start();

        logger.info("before interrupt()");
        thread.interrupt();
        logger.info("after interrupt()");

        thread.join();
    }
}
