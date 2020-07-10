package demo.part18_parallel_arrays;

import java.util.Arrays;

public class ParallelArraysDemo {

    public static void main(String[] args) {
        Integer[] array1 = {16, 7, 26, 14, 77};
        Arrays.parallelSort(array1);
        System.out.println(Arrays.toString(array1));

        Integer[] array2 = {16, 7, 26, 14, 77};
        Arrays.parallelPrefix(array2, (a, b) -> a * b);
        System.out.println(Arrays.toString(array2));

        Integer[] array3 = new Integer[7];
        Arrays.parallelSetAll(array3, i -> i);
        System.out.println(Arrays.toString(array3));
    }
}
