package demo.part09_atomic;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

public class LongAccumulatorDemo extends Demo2 {

    public static void main(String[] args) {
        accumulate();
    }

    private static void accumulate() {
        LongBinaryOperator function = (x, y) -> 2 * x + y;
        LongAccumulator longAccumulator = new LongAccumulator(function, 1L);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10)
                .forEach(i -> executorService.submit(() -> longAccumulator.accumulate(i)));

        shutdown(executorService);

        System.out.println("result of getThenReset(): " + longAccumulator.getThenReset());
    }
}
