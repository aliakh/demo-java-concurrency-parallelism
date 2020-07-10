package demo.part02_thread.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadYieldExercise {

    private static final Logger logger = LoggerFactory.getLogger(ThreadYieldExercise.class);

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                logger.info("thread 1: {}", i);
                Thread.yield();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                logger.info("thread 2: {}", i);
                Thread.yield();
            }
        });

        thread1.start();
        thread2.start();
    }
}
