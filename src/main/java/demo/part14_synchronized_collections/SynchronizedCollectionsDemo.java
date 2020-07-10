package demo.part14_synchronized_collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SynchronizedCollectionsDemo {

    public static void main(String[] args) {
        Collection<String> collection = Collections.synchronizedCollection(Arrays.asList());
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());

        Set<Integer> set = Collections.synchronizedSet(new HashSet<>());
        SortedSet<Integer> sortedSet = Collections.synchronizedSortedSet(new TreeSet<>());

        Map<Integer, String> map = Collections.synchronizedMap(new HashMap<>());
        SortedMap<Integer, String> sortedMap = Collections.synchronizedSortedMap(new TreeMap<>());
    }
}
