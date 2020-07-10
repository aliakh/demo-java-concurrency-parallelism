package demo.part19_utilities.part2;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomDemo {

    public static void main(String[] args) {
        System.out.println(ThreadLocalRandom.current().nextInt(100));
    }
}
