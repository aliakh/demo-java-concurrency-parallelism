package demo.part02_thread.part2;

public class ThreadCurrentThreadExercise {

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();

        System.out.println("current thread: " + thread);
        System.out.println();

        System.out.println("id: " + thread.getId());
        System.out.println("name: " + thread.getName());
        System.out.println("part3: " + thread.getState());
        System.out.println("priority: " + thread.getPriority());
        System.out.println("is alive: " + thread.isAlive());
        System.out.println("is interrupted: " + thread.isInterrupted());
        System.out.println("is daemon: " + thread.isDaemon());
        System.out.println("thread group: " + thread.getThreadGroup());
        System.out.println();

        System.out.println("uncaught exception handler: " + thread.getUncaughtExceptionHandler());
        System.out.println("default uncaught exception handler: " + Thread.getDefaultUncaughtExceptionHandler());
    }
}
