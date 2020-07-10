package demo.part16_fork_join.part4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class FibonacciRecursiveTask2 extends RecursiveTask<Long> {

    private final int number;
    private final int threshold;

    FibonacciRecursiveTask2(int number, int threshold) {
        this.number = number;
        this.threshold = threshold;
    }

    @Override
    protected Long compute() {
        if (number <= threshold) {
            return fibonacciRecursive(number);
        }
        FibonacciRecursiveTask2 f1 = new FibonacciRecursiveTask2(number - 1, threshold);
        f1.fork();
        FibonacciRecursiveTask2 f2 = new FibonacciRecursiveTask2(number - 2, threshold);
        return f2.compute() + f1.join();
    }

    private static long fibonacciRecursive(long n) {
        if (n <= 1) return n;
        else return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 30;

        System.out.println("recursive result: " + fibonacciRecursive(n));

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        FibonacciRecursiveTask2 task = new FibonacciRecursiveTask2(n, 1);

        fjp.execute(task);

        do {
            System.out.println("active threads count: " + fjp.getActiveThreadCount());
            System.out.println("thread pool size: " + fjp.getPoolSize());
            System.out.println("parallelism level: " + fjp.getParallelism());
            System.out.println("submitted tasks count: " + fjp.getQueuedSubmissionCount());
            System.out.println("stolen tasks count: " + fjp.getStealCount());
            Thread.sleep(100);
        } while (!task.isDone());

        System.out.println("Fork/Join result: " + task.getRawResult());
    }
}
