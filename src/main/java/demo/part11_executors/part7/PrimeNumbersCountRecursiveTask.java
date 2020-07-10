package demo.part11_executors.part7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class PrimeNumbersCountRecursiveTask extends RecursiveTask<Long> {

    private final int start;
    private final int end;
    private final int threshold;

    public PrimeNumbersCountRecursiveTask(int start, int end, int threshold) {
        this.start = start;
        this.end = end;
        this.threshold = threshold;
    }

    @Override
    protected Long compute() {
        if (((end + 1) - start) > threshold) {
            return ForkJoinTask.invokeAll(getSubTasks())
                    .stream()
                    .mapToLong(ForkJoinTask::join)
                    .sum();
        } else {
            return findPrimeNumbersCount();
        }
    }

    private List<PrimeNumbersCountRecursiveTask> getSubTasks() {
        List<PrimeNumbersCountRecursiveTask> tasks = new ArrayList<>();
        for (int i = 1; i <= end / threshold; i++) {
            int end = i * threshold;
            int start = (end - threshold) + 1;
            tasks.add(new PrimeNumbersCountRecursiveTask(start, end, threshold));
        }
        return tasks;
    }

    private long findPrimeNumbersCount() {
        long numbers = 0;
        for (int n = start; n <= end; n++) {
            if (isPrimeNumber(n)) {
                numbers++;
            }
        }
        return numbers;
    }

    private boolean isPrimeNumber(int n) {
        if (n == 2) {
            return true;
        }

        if (n == 1 || n % 2 == 0) {
            return false;
        }

        int divisors = 0;
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                divisors++;
            }
        }
        return divisors == 2;
    }
}
