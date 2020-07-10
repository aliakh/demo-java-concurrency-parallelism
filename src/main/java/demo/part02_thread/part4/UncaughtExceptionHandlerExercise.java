package demo.part02_thread.part4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sets an uncaught exception handler for a thread.
 */
public class UncaughtExceptionHandlerExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(UncaughtExceptionHandlerExercise.class);

    @Override
    public void run() {
        logger.info("parsed: {}", Integer.parseInt("1"));
        logger.info("parsed: {}", Integer.parseInt("two")); // java.lang.NumberFormatException: For input string: "two"
        logger.info("parsed: {}", Integer.parseInt("3")); // never happens
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new UncaughtExceptionHandlerExercise();
        thread.setUncaughtExceptionHandler(new ExceptionHandler());
        thread.start();

        thread.join();
        logger.info("finished");
    }

    private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("thread: {}", t);
            logger.error("exception: {}", e.toString());
        }
    }
}
