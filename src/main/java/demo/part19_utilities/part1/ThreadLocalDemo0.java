package demo.part19_utilities.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalDemo0 extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalDemo0.class);

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public void run() {
        logger.info("before setting a value in thread 1: " + threadLocal.get());
        threadLocal.set("Alpha");
        logger.info("after setting a value in thread 1: " + threadLocal.get());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new ThreadLocalDemo0();

        logger.info("before setting a value in thread 2: " + threadLocal.get());
        threadLocal.set("Zulu");

        thread.start();

        Thread.sleep(1000);

        logger.info("after setting a value in thread 2: " + threadLocal.get());

        thread.join();
    }
}