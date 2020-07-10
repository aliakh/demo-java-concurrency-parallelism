package demo.part11_executors.part0;

import java.util.concurrent.Callable;

/**
 * Uses the Callable interface.
 */
public class CallableExercise {

    public static void main(String[] args) throws Exception {
        Callable<Integer> callable1 = () -> Integer.valueOf("0");
        System.out.println("parsed: " + callable1.call());

        Callable<Integer> callable2 = new Callable<Integer>() {
            @Override
            public Integer call() throws NumberFormatException {
                return Integer.valueOf("1");
            }
        };
        System.out.println("parsed: " + callable2.call());
    }
}
