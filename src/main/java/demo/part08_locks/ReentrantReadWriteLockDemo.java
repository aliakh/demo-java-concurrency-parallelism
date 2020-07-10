package demo.part08_locks;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReentrantReadWriteLockDemo extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(ReentrantReadWriteLockDemo.class);

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static String value = "";

    private static void readValue() {
        ReadLock readLock = reentrantReadWriteLock.readLock();
        readLock.lock();
        try {
            logger.info("read: {}", value);
        } finally {
            readLock.unlock();
        }
    }

    private static void writeValue(String s) {
        WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
            logger.info("write: {}", s);
            value = value.concat(s);
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            Runnable reader = () -> {
                sleep(1);
                readValue();
            };
            executorService.submit(reader);
        }

        for (int i = 0; i < 5; i++) {
            AtomicInteger counter = new AtomicInteger(i);
            Runnable writer = () -> {
                writeValue(" " + counter.incrementAndGet());
            };
            executorService.execute(writer);
        }

        shutdown(executorService);
    }
}
