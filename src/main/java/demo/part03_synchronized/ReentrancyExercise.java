package demo.part03_synchronized;

public class ReentrancyExercise {

    private static final Object monitor = new Object();

    public static void main(String[] args) {
        System.out.println("before reentrancy");

        synchronized (monitor) {
            synchronized (monitor) {
                synchronized (monitor) {
                    System.out.println("inside reentrancy");
                }
            }
        }

        System.out.println("after reentrancy");
    }
}
