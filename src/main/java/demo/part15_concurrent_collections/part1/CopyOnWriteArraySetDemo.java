package demo.part15_concurrent_collections.part1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteArraySetDemo {

    public static void main(String[] args) {
        CopyOnWriteArraySet<Integer> list = new CopyOnWriteArraySet<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator1 = list.iterator();

        System.out.println("set before changes: " + list);


        list.add(4);
        Iterator<Integer> iterator2 = list.iterator();

        System.out.println("set after changes: " + list);

        System.out.println("iterator 1: ");
        iterator1.forEachRemaining(System.out::println);

        System.out.println("iterator 2: ");
        iterator2.forEachRemaining(System.out::println);


        Iterator<Integer> itr3 = list.iterator();
        while (itr3.hasNext()) {
            itr3.remove(); // java.lang.UnsupportedOperationException
        }
    }
}
