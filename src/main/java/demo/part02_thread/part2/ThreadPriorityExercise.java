package demo.part02_thread.part2;

import demo.part02_thread.part3.ThreadStateWaitingExercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPriorityExercise {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStateWaitingExercise.class);

    private static volatile boolean active = true;

    public static void main(String[] args) throws InterruptedException {
        for (int priority = Thread.MIN_PRIORITY; priority <= Thread.MAX_PRIORITY; priority++) {
            Thread thread = new Thread(new Runner(), "thread " + priority);
            thread.setPriority(priority);
            thread.start();
        }

        Thread.sleep(5000);

        active = false;
    }

    private static class Runner implements Runnable {

        private Integer counter = 0;

        @Override
        public void run() {
            while (active) {
                logger.info("counter: {}", ++counter);
            }
            logger.info("final counter: {}", counter);
        }
    }
}
