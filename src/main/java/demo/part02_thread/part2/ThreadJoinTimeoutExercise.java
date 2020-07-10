package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Uses Thread.join() method.
 */
public class ThreadJoinTimeoutExercise {

    private static final Logger logger = LoggerFactory.getLogger(ThreadJoinTimeoutExercise.class);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                logger.info("run() started");
                Thread.sleep(2000);
                logger.info("run() finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

        LocalDateTime start = LocalDateTime.now();
        logger.info("before join()");

        thread.join(1000);

        LocalDateTime finish = LocalDateTime.now();
        logger.info("after join(): {} millisecond(s)", Duration.between(start, finish).toMillis());
    }
}
