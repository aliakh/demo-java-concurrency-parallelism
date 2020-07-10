package demo.part01_processes_and_threads;

import java.util.Set;

/**
 * Gets list of all threads in the JVM.
 */
public class AllThreadsDemo {

    public static void main(String[] args) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread thread : threads) {
            System.out.println(thread);
        }
    }
}
