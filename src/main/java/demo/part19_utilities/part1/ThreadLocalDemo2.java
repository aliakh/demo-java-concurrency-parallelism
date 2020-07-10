package demo.part19_utilities.part1;

import demo.part02_thread.part3.ThreadStateWaitingExercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalDemo2 {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStateWaitingExercise.class);

    private static volatile ThreadLocal<Integer> counters = ThreadLocal.withInitial(() -> 0);
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

        @Override
        public void run() {
            while (active) {
                counters.set(counters.get() + 1);
                logger.info("counter: {}", counters.get());
            }
            logger.info("final counter: {}", counters.get());
        }
    }
}
