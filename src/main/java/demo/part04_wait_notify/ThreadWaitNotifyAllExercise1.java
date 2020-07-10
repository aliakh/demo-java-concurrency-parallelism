package demo.part04_wait_notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ThreadWaitNotifyAllExercise1 {

    private static final Logger logger = LoggerFactory.getLogger(ThreadWaitNotifyAllExercise1.class);

    public static void main(String[] args) throws InterruptedException {
        Broker broker = new Broker();

        Thread producer = new Thread(new Producer(broker), "producer");
        Thread consumer = new Consumer(broker, "consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.interrupt();
    }

    private static class Broker {

        private volatile boolean signalReady = false;

        synchronized private void await() throws InterruptedException {
            while (!signalReady) {
                wait();
            }
            signalReady = false;
        }

        synchronized private void signal() {
            signalReady = true;
            notifyAll();
        }
    }

    private static class Consumer extends Thread {

        private final Broker broker;

        Consumer(Broker broker, String name) {
            super(name);
            this.broker = broker;
        }

        @Override
        public void run() {
            try {
                logger.info("awaiting started");
                while (!this.isInterrupted()) {
                    broker.await();
                    logger.info("awaited");
                    Thread.sleep(new Random().nextInt(3000));
                }
                logger.info("awaiting finished");
            } catch (InterruptedException e) {
                this.interrupt();
                logger.info("awaiting interrupted");
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
                logger.info("signaling started");
                for (int i = 3; i >= 0; i--) {
                    broker.signal();
                    logger.info("signal: {}", i);
                    Thread.sleep(new Random().nextInt(3000));
                }
                logger.info("signaling finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

// http://tutorials.jenkov.com/java-concurrency/thread-signaling.html