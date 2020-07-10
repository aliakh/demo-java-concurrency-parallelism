package demo.part02_thread.part4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sets an uncaught exception handler for all threads.
 */
public class DefaultUncaughtExceptionHandlerExercise extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUncaughtExceptionHandlerExercise.class);

    @Override
    public void run() {
        logger.info("parsed: {}", Integer.parseInt("1"));
        logger.info("parsed: {}", Integer.parseInt("two")); // java.lang.NumberFormatException: For input string: "two"
        logger.info("parsed: {}", Integer.parseInt("3")); // never happens
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        Thread thread = new DefaultUncaughtExceptionHandlerExercise();
        thread.start();

        thread.join();
        logger.info("finished");
    }

    private static class ExceptionHandler implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("thread: {}", t);
            logger.error("exception: {}", e.toString());
        }
    }
}
