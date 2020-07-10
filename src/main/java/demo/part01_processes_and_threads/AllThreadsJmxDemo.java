package demo.part01_processes_and_threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class AllThreadsJmxDemo {

    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();
        for (long threadId : threadIds) {
            System.out.println(threadMXBean.getThreadInfo(threadId));
        }
    }
}
