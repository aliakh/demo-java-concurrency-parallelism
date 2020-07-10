package demo.part15_concurrent_collections.part2;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class _ConcurrentSkipListSetDemo {

    public static void main(String[] args) {
        NavigableSet<Integer> set = new ConcurrentSkipListSet<>();
    }
}
