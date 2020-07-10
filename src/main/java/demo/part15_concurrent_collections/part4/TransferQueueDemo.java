package demo.part15_concurrent_collections.part4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TransferQueueDemo {

    private static final Logger logger = LoggerFactory.getLogger(TransferQueueDemo.class);

    public static void main(String[] args) {
        TransferQueue<Integer> queue = new LinkedTransferQueue<>();

        int poisonPill = -1;
        new Thread(new Producer(queue, poisonPill), "producer").start();
        new Thread(new Consumer(queue, poisonPill), "consumer").start();
    }

    public static class Consumer implements Runnable {

        private final TransferQueue<Integer> queue;
        private final int poisonPill;

        public Consumer(TransferQueue<Integer> queue, Integer poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    logger.info("before read");
                    Integer message = queue.take();
                    logger.info("after read: {}", message);
                    Thread.sleep(1000);

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

        private final TransferQueue<Integer> queue;
        private final int poisonPill;

        public Producer(TransferQueue<Integer> queue, int poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; i++) {
                    logger.info("before write");
                    queue.transfer(i);
                    logger.info("after write: {}", i);
                }

                queue.put(poisonPill);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
