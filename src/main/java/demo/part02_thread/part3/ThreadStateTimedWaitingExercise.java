package demo.part02_thread.part3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadStateTimedWaitingExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStateTimedWaitingExercise.class);

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new ThreadStateTimedWaitingExercise();
        thread.start();

        Thread.sleep(1000);
        logger.info("part3={}", thread.getState());
    }
}