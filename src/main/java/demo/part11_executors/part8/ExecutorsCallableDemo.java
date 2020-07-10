package demo.part11_executors.part8;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class ExecutorsCallableDemo {

    public static void main(String[] args) throws Exception {
        Runnable runnable1 = () -> System.out.println("runnable 1...");
        Callable<Object> callable1 = Executors.callable(runnable1);
        System.out.println("callable 1 result: " + callable1.call());

        Runnable runnable2 = () -> System.out.println("runnable 2...");
        Callable<Integer> callable2 = Executors.callable(runnable2, 1);
        System.out.println("callable 2 result: " + callable2.call());
    }
}
