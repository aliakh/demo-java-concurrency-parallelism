package demo.part15_concurrent_collections.part4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueDemo {

    private static final Logger logger = LoggerFactory.getLogger(ArrayBlockingQueueDemo.class);

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        int poisonPill = -1;
        new Thread(new LinkedBlockingQueueDemo.Producer(queue, poisonPill), "producer").start();
        new Thread(new LinkedBlockingQueueDemo.Consumer(queue, poisonPill), "consumer").start();
    }

    public static class Consumer implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final int poisonPill;

        public Consumer(BlockingQueue<Integer> queue, Integer poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Integer message = queue.take();
                    logger.info("read: {}", message);
                    Thread.sleep(500);

                    if (message == poisonPill) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class Producer implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final int poisonPill;

        public Producer(BlockingQueue<Integer> queue, int poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; i++) {
                    logger.info("write: {}", i);
                    queue.put(i);

                    logger.info("remaining capacity: {}", queue.remainingCapacity());
                    Thread.sleep(100);
                }

                queue.put(poisonPill);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
