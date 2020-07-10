package demo.part09_atomic;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class LongAdderDemo extends Demo2 {

    public static void main(String[] args) {
        add();
        increment();
    }

    private static void add() {
        LongAdder longAdder = new LongAdder();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10000)
                .forEach(i -> executor.submit(() -> longAdder.add(2)));

        shutdown(executor);

        System.out.println("result of add(): " + longAdder.sumThenReset());
    }

    private static void increment() {
        LongAdder longAdder = new LongAdder();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10000)
                .forEach(i -> executor.submit(longAdder::increment));

        shutdown(executor);

        System.out.println("result of increment(): " + longAdder.sumThenReset());
    }
}
