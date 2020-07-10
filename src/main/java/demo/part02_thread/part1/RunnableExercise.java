package demo.part02_thread.part1;

/**
 * Uses the Runnable interface.
 */
public class RunnableExercise {

    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println("run...");
        runnable.run();
    }
}
