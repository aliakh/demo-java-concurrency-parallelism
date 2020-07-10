package demo.part16_fork_join.part4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class FibonacciRecursiveTask1 extends RecursiveTask<Long> {

    private final int number;
    private final int threshold;

    FibonacciRecursiveTask1(int number, int threshold) {
        this.number = number;
        this.threshold = threshold;
    }

    @Override
    protected Long compute() {
        if (number <= threshold) {
            return fibonacciRecursive(number);
        }
        FibonacciRecursiveTask1 f1 = new FibonacciRecursiveTask1(number - 1, threshold);
        f1.fork();
        FibonacciRecursiveTask1 f2 = new FibonacciRecursiveTask1(number - 2, threshold);
        return f2.compute() + f1.join();
    }

    private static long fibonacciRecursive(long n) {
        if (n <= 1) return n;
        else return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    public static void main(String[] args) {
        int n = 10;

        System.out.println("recursive result: " + fibonacciRecursive(n));

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        FibonacciRecursiveTask1 task = new FibonacciRecursiveTask1(n, 1);
        System.out.println("Fork/Join result: " + fjp.invoke(task));
    }
}
