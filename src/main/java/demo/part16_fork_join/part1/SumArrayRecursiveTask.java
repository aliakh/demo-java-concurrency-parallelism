package demo.part16_fork_join.part1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

class SumArrayRecursiveTask extends RecursiveTask<Long> {

    private final int threshold;
    private final long[] array;
    private final int start, end;

    SumArrayRecursiveTask(int threshold, long[] array) {
        this(threshold, array, 0, array.length);
    }

    private SumArrayRecursiveTask(int threshold, long[] array, int start, int end) {
        this.threshold = threshold;
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= threshold) {
            return sum();
        } else {
            SumArrayRecursiveTask task1 = new SumArrayRecursiveTask(threshold, array, start, start + length / 2);
            task1.fork();

            SumArrayRecursiveTask task2 = new SumArrayRecursiveTask(threshold, array, start + length / 2, end);

            Long result1 = task1.join();
            Long result2 = task2.compute();

            return result1 + result2;
        }
    }

    private long sum() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        int n = 100;
        long[] array = LongStream.rangeClosed(1, n).toArray();

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        SumArrayRecursiveTask task = new SumArrayRecursiveTask(10, array);

        System.out.println("sum: " + fjp.invoke(task));
    }
}
