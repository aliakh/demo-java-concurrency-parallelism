package demo.part05_volatile;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class IncrementVolatileFieldDemo extends Demo2 {

    private static volatile int count;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(() -> count++));

        shutdown(executorService);

        System.out.println(count); // less than 10000
    }
}
