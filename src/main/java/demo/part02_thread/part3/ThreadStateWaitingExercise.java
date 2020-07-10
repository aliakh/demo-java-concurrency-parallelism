package demo.part02_thread.part3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadStateWaitingExercise {

    private static final Logger logger = LoggerFactory.getLogger(ThreadStateWaitingExercise.class);

    public static void main(String[] args) throws InterruptedException {
        Broker broker = new Broker();

        Thread producer = new Thread(new Producer(broker));
        Thread consumer = new Consumer(broker);

        producer.start();
        consumer.start();

        Thread.sleep(1000);
        logger.info("state={}", producer.getState());
        logger.info("state={}", consumer.getState());
    }

    private static class Broker {

        private volatile boolean ready = false;

        synchronized private void await() throws InterruptedException {
            while (!ready) {
                wait();
            }
            ready = false;
        }

        synchronized private void signal() {
            ready = true;
            notifyAll();
        }
    }

    private static class Consumer extends Thread {

        private final Broker broker;

        Consumer(Broker broker) {
            this.broker = broker;
        }

        @Override
        public void run() {
            try {
                broker.await();
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
                Thread.sleep(2000);
                broker.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
