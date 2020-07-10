package demo.part11_executors.part1;

import demo.common.Demo2;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NewSingleThreadExecutorDemo extends Demo2 {

    public static void main(String[] args) {
        Runnable runnable = () -> logger.info("run...");

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(runnable);
    }
}
