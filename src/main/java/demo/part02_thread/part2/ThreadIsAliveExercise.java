package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses Thread.isAlive() method.
 */
public class ThreadIsAliveExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadIsAliveExercise.class);

    @Override
    public void run() {
        try {
            logger.info("inside run(): isAlive={}", this.isAlive());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new ThreadIsAliveExercise();

        logger.info("before start(): isAlive={}", thread.isAlive());
        thread.start();
        logger.info("after start(): isAlive={}", thread.isAlive());

        thread.join();
        logger.info("after join(): isAlive={}", thread.isAlive());
    }
}
