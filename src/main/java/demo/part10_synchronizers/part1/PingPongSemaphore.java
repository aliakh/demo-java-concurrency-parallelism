package demo.part10_synchronizers.part1;

import java.util.concurrent.Semaphore;

public class PingPongSemaphore {

    private final static Semaphore mutexPing = new Semaphore(1);
    private final static Semaphore mutexPong = new Semaphore(0);

    private final static int COUNT = 3;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start");

        PingPongThread ping = new PingPongThread("Ping!", COUNT, mutexPing, mutexPong);
        PingPongThread pong = new PingPongThread("Pong!", COUNT, mutexPong, mutexPing);
        ping.start();
        pong.start();
        ping.join();
        pong.join();

        System.out.println("Fihish");
    }

    private static class PingPongThread extends Thread {

        private final String message;
        private final Semaphore mine;
        private final Semaphore other;
        private final int count;

        public PingPongThread(String message, int count, Semaphore mine, Semaphore other) {
            this.message = message;
            this.count = count;
            this.mine = mine;
            this.other = other;
        }

        @Override
        public void run() {
            for (int i = 1; i <= count; i++) {
                mine.acquireUninterruptibly();

                System.out.println(message + " " + i);

                other.release();
            }
        }
    }
}
