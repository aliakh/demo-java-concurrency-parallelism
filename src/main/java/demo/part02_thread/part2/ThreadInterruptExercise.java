package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interrupts a Thread.
 */
public class ThreadInterruptExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadInterruptExercise.class);

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            logger.info("run: {}", System.nanoTime());
        }
        logger.info("is interrupted");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new ThreadInterruptExercise();
        thread.start();

        Thread.sleep(1000);

        logger.info("before interrupt()");
        thread.interrupt();
        logger.info("after interrupt()");

        thread.join();
    }
}
