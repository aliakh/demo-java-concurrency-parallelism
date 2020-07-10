package demo.part15_concurrent_collections.part1;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(new Integer[]{1, 2, 3});
        Iterator<Integer> iterator1 = list.iterator();

        System.out.println("list before changes: " + list);


        list.add(4);
        Iterator<Integer> iterator2 = list.iterator();

        System.out.println("list after changes: " + list);

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
