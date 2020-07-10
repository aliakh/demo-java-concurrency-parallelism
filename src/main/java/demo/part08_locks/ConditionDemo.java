package demo.part08_locks;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {

    private static final Logger logger = LoggerFactory.getLogger(ConditionDemo.class);

    public static void main(String[] args) {
        Broker broker = new Broker();
        new Producer(broker).start();
        new Consumer(broker).start();
    }

    private static class Broker {

        private volatile boolean ready = false;
        private int message;

        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        int receive() throws InterruptedException {
            lock.lock();
            try {
                while (!ready) {
                    condition.await();
                }

                ready = false;
                condition.signalAll();

                return message;
            } finally {
                lock.unlock();
            }
        }

        void send(int message) throws InterruptedException {
            lock.lock();
            try {
                while (ready) {
                    condition.await();
                }

                ready = true;
                this.message = message;

                condition.signalAll();
            } finally {
                lock.unlock();
            }
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

    private static class Producer extends Thread {

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

