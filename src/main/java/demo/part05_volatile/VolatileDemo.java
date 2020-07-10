package demo.part05_volatile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class VolatileDemo extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(VolatileDemo.class);

    private volatile boolean run = true;

    @Override
    public void run() {
        while (run) {
            logger.info("run: {}", LocalDateTime.now());
        }
        logger.info("is stopped");
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileDemo exercise = new VolatileDemo();
        exercise.start();

        Thread.sleep(1000);

        logger.info("before stopping");
        exercise.run = false;
        logger.info("after stopping");

        exercise.join();
    }
}
