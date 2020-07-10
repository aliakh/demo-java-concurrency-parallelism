package demo.part16_fork_join.part1;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.LongStream;

class IncrementArrayRecursiveAction extends RecursiveAction {

    private final int threshold;
    final long[] array;
    final int start, end;

    IncrementArrayRecursiveAction(int threshold, long[] array) {
        this(threshold, array, 0, array.length);
    }

    IncrementArrayRecursiveAction(int threshold, long[] array, int start, int end) {
        this.threshold = threshold;
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start < threshold) {
            for (int i = start; i < end; ++i) {
                array[i]++;
            }
        } else {
            int middle = (start + end) / 2;
            IncrementArrayRecursiveAction task1 = new IncrementArrayRecursiveAction(threshold, array, start, middle);
            IncrementArrayRecursiveAction task2 = new IncrementArrayRecursiveAction(threshold, array, middle, end);
            invokeAll(task1, task2);
        }
    }

    public static void main(String[] args) {
        int n = 100;
        long[] array = LongStream.rangeClosed(1, n).toArray();

        System.out.println("array: " + Arrays.toString(array));

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        IncrementArrayRecursiveAction task = new IncrementArrayRecursiveAction(10, array);
        fjp.invoke(task);

        System.out.println("array: " + Arrays.toString(array));
    }
}
