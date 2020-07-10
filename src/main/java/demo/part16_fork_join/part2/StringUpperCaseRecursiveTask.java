package demo.part16_fork_join.part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class StringUpperCaseRecursiveTask extends RecursiveTask<String> {

    private static final Logger logger = LoggerFactory.getLogger(StringUpperCaseRecursiveTask.class);

    private final int threshold;
    private final String argument;

    public StringUpperCaseRecursiveTask(int threshold, String argument) {
        this.threshold = threshold;
        this.argument = argument;
        logger.info("create task: " + argument);
    }

    @Override
    protected String compute() {
        if (argument.length() < threshold) {
            String result = argument.toUpperCase();
            logger.info("result: {}", result);
            return result;
        } else {
            StringUpperCaseRecursiveTask task1 = new StringUpperCaseRecursiveTask(threshold, argument.substring(0, argument.length() / 2));
            StringUpperCaseRecursiveTask task2 = new StringUpperCaseRecursiveTask(threshold, argument.substring(argument.length() / 2));
            ForkJoinTask.invokeAll(task1, task2);
            String result1 = task1.compute();
            String result2 = task2.compute();
            String target = result1 + result2;
            logger.info("join results: {}", result1 + " " + result2);
            return target;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool fjp = ForkJoinPool.commonPool();
        StringUpperCaseRecursiveTask task = new StringUpperCaseRecursiveTask(4, "abcdefghijklmnopqrstuvwxyz");
        fjp.invoke(task);
    }
}
