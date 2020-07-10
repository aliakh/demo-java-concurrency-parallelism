package demo.part08_locks;

import demo.common.Demo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ReentrantLockDemo extends Demo2 {

    private static final Logger logger = LoggerFactory.getLogger(ReentrantLockDemo.class);

    private static final ReentrantLock lock = new ReentrantLock();
    private static int count;

    private static void increment() {
        try {
            if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                logger.info("is locked by current thread: {}", lock.isHeldByCurrentThread());
            } else {
                logger.info("locked");
                try {
                    logger.info("is locked by current thread: {}", lock.isHeldByCurrentThread());
                    count++;
                    TimeUnit.SECONDS.sleep(3);
                } finally {
                    logger.info("unlocked");
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 3)
                .forEach(i -> executorService.submit(ReentrantLockDemo::increment));

        shutdown(executorService);

        logger.info("count: {}", count); // 1
    }
}
