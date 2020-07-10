package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExecuteDemo extends Demo2 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = () -> System.out.println("run...");
        executorService.execute(runnable);

        shutdown(executorService);
    }
}
