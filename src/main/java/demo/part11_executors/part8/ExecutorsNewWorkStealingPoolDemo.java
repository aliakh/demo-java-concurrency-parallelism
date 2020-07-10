package demo.part11_executors.part8;

import demo.common.Demo2;
import demo.part11_executors.part7.PrimeNumbersCountRecursiveTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

// https://www.wolframalpha.com/input/?i=number+of+prime+numbers+between+1+to+10000
// http://oeis.org/A006880
// https://primes.utm.edu/howmany.html
// https://en.wikipedia.org/wiki/Prime-counting_function
public class ExecutorsNewWorkStealingPoolDemo extends Demo2 {

    public static void main(String[] args) {
        ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
        ForkJoinPool forkJoinPool = (ForkJoinPool) Executors.newWorkStealingPool();

        Long count = forkJoinPool.invoke(task);
        System.out.println("count: " + count); // 9592
    }
}
