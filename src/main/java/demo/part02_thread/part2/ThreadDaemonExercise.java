package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

public class ThreadDaemonExercise {

    private static final Logger logger = LoggerFactory.getLogger(ThreadInterruptedExceptionExercise.class);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    logger.info("run...: {}", LocalTime.now());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        Thread.sleep(3000);
    }
}
