package demo.part03_synchronized;

public class ThreadHoldsLockExercise {

    private static final Object monitor = new Object();

    public static void main(String[] args) {
        System.out.println("is lock held: " + Thread.holdsLock(monitor));
        synchronized (monitor) {
            System.out.println("is lock held: " + Thread.holdsLock(monitor));
            synchronized (monitor) {
                System.out.println("is lock held: " + Thread.holdsLock(monitor));
                synchronized (monitor) {
                    System.out.println("is lock held: " + Thread.holdsLock(monitor));
                }
                System.out.println("is lock held: " + Thread.holdsLock(monitor));
            }
            System.out.println("is lock held: " + Thread.holdsLock(monitor));
        }
        System.out.println("is lock held: " + Thread.holdsLock(monitor));
    }
}
