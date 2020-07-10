package demo.part10_synchronizers.part3;

import demo.common.Demo2;

import java.util.concurrent.Exchanger;

public class ExchangerDemo extends Demo2 {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        Runner runner1 = new Runner(exchanger, "Alpha");
        Runner runner2 = new Runner(exchanger, "Omega");

        new Thread(runner1).start();
        new Thread(runner2).start();
    }

    private static class Runner implements Runnable {

        private final Exchanger<String> exchanger;
        private String value;

        public Runner(Exchanger<String> exchanger, String value) {
            this.exchanger = exchanger;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                String newValue = value;
                value = exchanger.exchange(value);

                logger.info("exchanged " + newValue + " for " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
