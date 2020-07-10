package demo.part03_synchronized;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SynchronizedInstanceBlockDemo extends Demo2 {

    private int count;

    private void increment() {
        synchronized (this) {
            count++;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        SynchronizedInstanceBlockDemo demo = new SynchronizedInstanceBlockDemo();

        IntStream.range(0, 10000)
                .forEach(i -> executorService.submit(demo::increment));

        shutdown(executorService);

        System.out.println("count: " + demo.count); // 10000
    }
}
