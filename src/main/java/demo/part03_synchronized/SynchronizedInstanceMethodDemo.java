package demo.part03_synchronized;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SynchronizedInstanceMethodDemo extends Demo2 {

    private int count;

    private synchronized void increment() {
        count++;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        SynchronizedInstanceMethodDemo demo = new SynchronizedInstanceMethodDemo();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(demo::increment));

        shutdown(executorService);

        System.out.println("count: " + demo.count); // 10000
    }
}
