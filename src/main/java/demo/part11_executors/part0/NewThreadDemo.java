package demo.part11_executors.part0;

import demo.common.Demo0;

public class NewThreadDemo extends Demo0 {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() { // task creation
            @Override
            public void run() {
                logger.info("run..."); // task execution
            }
        };

        Thread thread = new Thread(runnable); // task submission, thread creation
        thread.start(); // thread start

        thread.join(); // thread termination
    }
}
