package demo.part11_executors.part2;

import demo.common.Demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceIsShutdownIsTerminatedDemo extends Demo2 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> sleep(1));
        }

        logShutdownProgress(executorService);
        executorService.shutdown();
        logShutdownProgress(executorService);

        for (int i = 0; i < 10; i++) {
            sleep(1);

            logShutdownProgress(executorService);

            if (executorService.isTerminated()) {
                logShutdownProgress(executorService);
                break;
            }
        }
    }

    private static void logShutdownProgress(ExecutorService executorService) {
        logger.info("isShutdown={} isTerminated={}",
                executorService.isShutdown(),
                executorService.isTerminated()
        );
    }
}

