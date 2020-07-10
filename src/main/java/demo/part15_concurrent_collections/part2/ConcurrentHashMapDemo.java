package demo.part15_concurrent_collections.part2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
        map.put(1, "one");

        System.out.println(map.getOrDefault(2, "two"));

        map.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer key, String value) {
                System.out.println(key + "=" + value);
            }
        });

        map.replaceAll(new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer key, String value) {
                return value.toUpperCase();
            }
        });
        System.out.println(map);

        map.computeIfAbsent(2, new Function<Integer, String>() {
            @Override
            public String apply(Integer key) {
                return "two";
            }
        });
        System.out.println(map);

        map.computeIfPresent(1, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer key, String value) {
                return "one";
            }
        });
        System.out.println(map);

        map.compute(3, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer key, String value) {
                return "three";
            }
        });
        System.out.println(map);

        System.out.println(map.merge(1, "ONE", new BiFunction<String, String, String>() {
            @Override
            public String apply(String value1, String value2) {
                return value1 + " " + value2;
            }
        }));
        System.out.println(map);

        map.putIfAbsent(4, "four");
        System.out.println(map);

        map.remove(1, "one ONE");
        System.out.println(map);

        map.replace(2, "two", "TWO");
        System.out.println(map);

        map.replace(2, "two");
        System.out.println(map);
    }
}
