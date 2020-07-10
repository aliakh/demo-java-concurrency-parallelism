package demo.part04_wait_notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses the Thread.wait() and Thread.notifyAll() methods to send messages from one thread to another.
 */
public class ThreadWaitNotifyAllExercise2 {

    private static final Logger logger = LoggerFactory.getLogger(ThreadWaitNotifyAllExercise2.class);

    public static void main(String[] args) {
        Broker broker = new Broker();

        Thread producer = new Thread(new Producer(broker), "producer");
        Thread consumer = new Thread(new Consumer(broker), "consumer");

        producer.start();
        consumer.start();
    }

    private static class Broker {

        private volatile boolean messageReady = false;
        private int message;

        private synchronized int receive() throws InterruptedException {
            while (!messageReady) {
                wait();
            }
            messageReady = false;

            notifyAll();
            return message;
        }

        private synchronized void send(int message) throws InterruptedException {
            while (messageReady) {
                wait();
            }
            messageReady = true;

            this.message = message;
            notifyAll();
        }
    }

    private static class Consumer implements Runnable {

        private final Broker broker;

        Consumer(Broker broker) {
            this.broker = broker;
        }

        @Override
        public void run() {
            try {
                logger.info("receiving started");
                int i;
                do {
                    i = broker.receive();
                    logger.info("received: {}", i);
                } while (i != 0);
                logger.info("receiving finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Producer implements Runnable {

        private final Broker broker;

        Producer(Broker broker) {
            this.broker = broker;
        }

        @Override
        public void run() {
            try {
                logger.info("sending started");
                for (int i = 3; i >= -1; i--) {
                    broker.send(i);
                    logger.info("send: {}", i);
                }
                logger.info("sending finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
