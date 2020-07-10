package demo.part03_synchronized;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SynchronizedStaticMethodDemo extends Demo2 {

    private static int count;

    private static synchronized void increment() {
        count++;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(SynchronizedStaticMethodDemo::increment));

        shutdown(executorService);

        System.out.println("count: " + count); // 10000
    }
}
