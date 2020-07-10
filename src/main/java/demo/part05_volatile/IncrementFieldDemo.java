package demo.part05_volatile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class IncrementFieldDemo {

    private static int count;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(() -> count++));

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println(count); // less than 10000
    }
}
