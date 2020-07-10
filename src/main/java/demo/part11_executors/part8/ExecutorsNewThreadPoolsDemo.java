package demo.part11_executors.part8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsNewThreadPoolsDemo {

    public static void main(String[] args) {
        information(Executors.newSingleThreadExecutor());
        information(Executors.newFixedThreadPool(2));
        information(Executors.newCachedThreadPool());
        information(Executors.newSingleThreadScheduledExecutor());
        information(Executors.newScheduledThreadPool(2));
        information(Executors.newWorkStealingPool());
    }

    private static void information(ExecutorService executorService) {
        System.out.println(executorService);
    }
}
