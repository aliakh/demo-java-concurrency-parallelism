package demo.part08_locks;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo2 extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(StampedLockDemo2.class);

    private static final StampedLock stampedLock = new StampedLock();
    private static String value = "";

    private static void readValueOptimistic() {
        long stamp = stampedLock.tryOptimisticRead();
        if (stampedLock.validate(stamp)) {
            logger.info("read optimistically: {}", value);
        } else {
            stamp = stampedLock.readLock();
            try {
                sleep(5000);
                logger.info("read: {}", value);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }

    private static void writeValue(String s) {
        long stamp = stampedLock.writeLock();
        try {
            logger.info("write: {}", s);
            value = value.concat(s);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool(); // 2

        for (int i = 0; i < 5; i++) { // 2
            Runnable reader = () -> {
                sleep(1);
                readValueOptimistic();
            };
            executorService.submit(reader);
        }

        for (int i = 0; i < 5; i++) { // 1
            AtomicInteger counter = new AtomicInteger(i);
            Runnable writer = () -> {
                writeValue(" " + counter.incrementAndGet());
            };
            executorService.execute(writer);
        }

        shutdown(executorService);
    }
}

// https://www.javaspecialists.eu/archive/Issue215.html