package demo.part11_executors.part8;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorsUnconfigurableExecutorServiceDemo extends Demo2 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.unconfigurableExecutorService(
                new ThreadPoolExecutor(1, 2,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>())
        );

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService; // java.lang.ClassCastException
    }
}
