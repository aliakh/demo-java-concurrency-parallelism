package demo.part11_executors.part8;

import demo.common.Demo2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExecutorsUnconfigurableScheduledExecutorServiceDemo extends Demo2 {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.unconfigurableScheduledExecutorService(
                new ScheduledThreadPoolExecutor(1)
        );

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) scheduledExecutorService; // java.lang.ClassCastException
    }
}
