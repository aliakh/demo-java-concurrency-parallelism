package demo.part17_parallel_streams;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelStreamsDemo2 {

    public static void main(String[] args) {
        LocalDateTime start1 = LocalDateTime.now();

        IntStream.rangeClosed(0, 50_000)
                .mapToObj(BigInteger::valueOf)
                .map(ParallelStreamsDemo2::isPrime)
                .collect(Collectors.toList());

        LocalDateTime finish1 = LocalDateTime.now();
        System.out.println("sequential (milliseconds): " + Duration.between(start1, finish1).toMillis());

        LocalDateTime start2 = LocalDateTime.now();

        IntStream.rangeClosed(0, 50_000)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .map(ParallelStreamsDemo2::isPrime)
                .collect(Collectors.toList());

        LocalDateTime finish2 = LocalDateTime.now();
        System.out.println("parallel (milliseconds):   " + Duration.between(start2, finish2).toMillis());
    }

    // an CPU intensive calculation
    private static boolean isPrime(BigInteger n) {
        BigInteger counter = BigInteger.ONE.add(BigInteger.ONE);
        boolean isPrime = true;
        while (counter.compareTo(n) < 0) {
            if (n.remainder(counter).compareTo(BigInteger.ZERO) == 0) {
                isPrime = false;
                break;
            }
            counter = counter.add(BigInteger.ONE);
        }
        return isPrime;
    }
}
