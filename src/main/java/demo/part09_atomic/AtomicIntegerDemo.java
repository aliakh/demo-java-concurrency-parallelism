package demo.part09_atomic;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AtomicIntegerDemo extends Demo2 {

    public static void main(String[] args) {
        incrementAndGet();
        getAndIncrement();

        accumulateAndGet();
        getAndAccumulate();

        updateAndGet();
        getAndUpdate();

        // etc
    }

    private static void incrementAndGet() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(atomicInteger::incrementAndGet));

        shutdown(executorService);

        System.out.println("result of incrementAndGet(): " + atomicInteger.get());
    }

    private static void getAndIncrement() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(atomicInteger::getAndIncrement));

        shutdown(executorService);

        System.out.println("result of incrementAndGet(): " + atomicInteger.get());
    }

    private static void accumulateAndGet() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(() -> atomicInteger.accumulateAndGet(i, Integer::sum)));

        shutdown(executorService);

        System.out.println("result of accumulateAndGet(): " + atomicInteger.get());
    }

    private static void getAndAccumulate() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(() -> atomicInteger.getAndAccumulate(i, Integer::sum)));

        shutdown(executorService);

        System.out.println("result of accumulateAndGet(): " + atomicInteger.get());
    }

    private static void updateAndGet() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(() -> atomicInteger.updateAndGet(n -> n + 2)));

        shutdown(executorService);

        System.out.println("result of updateAndGet(): " + atomicInteger.get());
    }

    private static void getAndUpdate() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(() -> atomicInteger.getAndUpdate(n -> n + 2)));

        shutdown(executorService);

        System.out.println("result of updateAndGet(): " + atomicInteger.get());
    }
}
