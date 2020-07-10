package demo.part08_locks;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class LockDemo extends Demo2 {

    private static final Lock lock = new ReentrantLock();
    private static int count;

    private static void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(LockDemo::increment));

        shutdown(executorService);

        System.out.println("count: " + count); // 10000
    }
}
