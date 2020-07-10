package demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

public class Demo0 {

    protected static final Logger logger = LoggerFactory.getLogger(Demo0.class);

    protected static void logDuration(String name, Runnable2 runnable) throws Exception {
        LocalDateTime start = LocalDateTime.now();
        logger.info("before '{}'", name);
        runnable.run();
        logger.info("after '{}': {} millisecond(s)", name, Duration.between(start, LocalDateTime.now()).toMillis());
    }

    @FunctionalInterface
    public interface Runnable2 {
        void run() throws Exception;
    }
}
