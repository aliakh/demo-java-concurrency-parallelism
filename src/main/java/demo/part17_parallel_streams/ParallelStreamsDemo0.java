package demo.part17_parallel_streams;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelStreamsDemo0 {

    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).parallel().forEach(System.out::println);

        Stream.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}).parallel().count();

        LinkedList<Integer> list = new LinkedList<Integer>();
        list.add(100);
        list.add(200);
        list.add(300);
        Stream<Integer> stream1 = list.stream().parallel();
        Stream<Integer> stream2 = list.parallelStream();

        int[] values = IntStream.range(1000, 2000).parallel().toArray();

        Arrays.stream(new double[]{1.1, 2.2, 3.3}).parallel().sum();
    }
}
