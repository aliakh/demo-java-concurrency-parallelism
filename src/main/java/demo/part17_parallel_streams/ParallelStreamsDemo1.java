package demo.part17_parallel_streams;

import java.util.stream.IntStream;

public class ParallelStreamsDemo1 {

    public static void main(String[] args) {

        System.out.print("sequential: ");

        IntStream.rangeClosed(0, 10)
                .mapToObj(Integer::toString)
                .forEach(s -> System.out.print(s + " "));

        System.out.println();

        System.out.print("parallel:   ");

        IntStream.rangeClosed(0, 10)
                .parallel()
                .mapToObj(Integer::toString)
                .forEach(s -> System.out.print(s + " "));

        System.out.println();
    }
}
