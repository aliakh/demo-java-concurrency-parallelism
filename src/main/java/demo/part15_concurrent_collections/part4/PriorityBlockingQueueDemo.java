package demo.part15_concurrent_collections.part4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueDemo {

    private static final Logger logger = LoggerFactory.getLogger(PriorityBlockingQueueDemo.class);

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(10);

        int poisonPill = 8;
        new Thread(new Producer(queue, poisonPill), "producer").start();
        new Thread(new Consumer(queue, poisonPill), "consumer").start();
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
                queue.addAll(Arrays.asList(3, 5, 6, 1, 2, 4, 7));
                queue.put(poisonPill);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
