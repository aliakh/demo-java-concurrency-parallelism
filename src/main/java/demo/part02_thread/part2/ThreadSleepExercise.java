package demo.part02_thread.part2;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Uses Thread.sleep() method.
 */
public class ThreadSleepExercise {

    public static void main(String[] args) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();

        Thread.sleep(1000);

        LocalDateTime finish = LocalDateTime.now();
        System.out.println("sleep (milliseconds): " + Duration.between(start, finish).toMillis());
    }
}