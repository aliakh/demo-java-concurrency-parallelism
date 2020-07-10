package demo.part11_executors.part1;

import demo.common.Demo0;

import java.util.concurrent.Executor;

public class AsynchronousNewThreadExecutorDemo extends Demo0 {

    public static void main(String[] args) {
        Runnable runnable = () -> logger.info("run...");

        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };

        executor.execute(runnable);
    }
}

