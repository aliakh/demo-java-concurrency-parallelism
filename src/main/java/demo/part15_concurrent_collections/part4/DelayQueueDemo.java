package demo.part15_concurrent_collections.part4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo {

    private static final Logger logger = LoggerFactory.getLogger(DelayQueueDemo.class);

    public static void main(String[] args) {
        BlockingQueue<DelayedObject> queue = new DelayQueue<DelayedObject>();

        int poisonPill = -1;
        new Thread(new Producer(queue, poisonPill), "producer").start();
        new Thread(new Consumer(queue, poisonPill), "consumer").start();
    }

    public static class DelayedObject implements Delayed {

        private int data;
        private long delayMillis;
        private long startTime;

        DelayedObject(int data, long delayMillis) {
            this.data = data;
            this.delayMillis = delayMillis;
            this.startTime = System.currentTimeMillis() + delayMillis;
        }

        public int getData() {
            return data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(startTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed that) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), that.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "DelayedObject{" +
                    "data='" + data + '\'' +
                    ", delayMillis=" + delayMillis +
                    '}';
        }
    }

    public static class Consumer implements Runnable {

        private final BlockingQueue<DelayedObject> queue;
        private final int poisonPill;

        public Consumer(BlockingQueue<DelayedObject> queue, Integer poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    DelayedObject message = queue.take();
                    logger.info("read: {}", message);

                    if (message.getData() == poisonPill) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class Producer implements Runnable {

        private final BlockingQueue<DelayedObject> queue;
        private final int poisonPill;

        public Producer(BlockingQueue<DelayedObject> queue, int poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    DelayedObject message = new DelayedObject(i, i * 1000);
                    logger.info("write: {}", message);
                    queue.put(message);
                }
                for (int i = 10; i >= 5; i--) {
                    DelayedObject message = new DelayedObject(i, (15 - i) * 1000);
                    logger.info("write: {}", message);
                    queue.put(message);
                }

                queue.put(new DelayedObject(poisonPill, 15000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
