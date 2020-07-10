package demo.part01_processes_and_threads;

public class ProcessIdDemo {

    public static void main(String[] args) {
        ProcessHandle currentProcess = ProcessHandle.current();
        System.out.println("process id: " + currentProcess.pid());
        System.out.println("process information: " + currentProcess.info());
    }
}
