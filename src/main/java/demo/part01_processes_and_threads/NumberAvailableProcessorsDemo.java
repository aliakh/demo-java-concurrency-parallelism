package demo.part01_processes_and_threads;

/**
 * Gets number of processors available for the JVM.
 */
public class NumberAvailableProcessorsDemo {

    public static void main(String[] args) {
        System.out.println("Number of available processors: " + Runtime.getRuntime().availableProcessors());
    }
}
