package spliterators.part3.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class ZipWithArraySpliteratorTest {
    private final Integer[] ints = randomArray(1000);

    private final Integer[] shorter = randomArray(800);
    private final Integer[] exact = randomArray(1000);
    private final Integer[] longer = randomArray(1200);

    private Stream<Pair<Integer, Integer>> streamShorter = StreamSupport.stream(
            new ZipWithArraySpliterator<>(Arrays.stream(ints).spliterator(), shorter), true);

    private Stream<Pair<Integer, Integer>> streamExact = StreamSupport.stream(
            new ZipWithArraySpliterator<>(Arrays.stream(ints).spliterator(), exact), true);

    private Stream<Pair<Integer, Integer>> streamLonger = StreamSupport.stream(
            new ZipWithArraySpliterator<>(Arrays.stream(ints).spliterator(), longer), true);

    private long shorterExpected = getExpectedReduction(ints, shorter);
    private long exactExpected = getExpectedReduction(ints, exact);
    private long longerExpected = getExpectedReduction(ints, longer);

    private Integer[] randomArray(int limit) {
        return ThreadLocalRandom.current().ints(limit, 0, 1000).boxed().toArray(Integer[]::new);
    }

    private long getExpectedReduction(Integer[] a, Integer[] b) {
        long result = 0;
        for (int i = 0; i < Math.min(a.length, b.length); i++)
            result += a[i] * b[i] + 1;
        return result;
    }

    @Test
    public void testSpliteratorWithArrays() {
        long sum = streamShorter
                .mapToLong(p -> p.getA() * p.getB() + 1)
                .sum();
        assertEquals(shorterExpected, sum);

        sum = streamExact
                .mapToLong(p -> p.getA() * p.getB() + 1)
                .sum();
        assertEquals(exactExpected, sum);

        sum = streamLonger
                .mapToLong(p -> p.getA() * p.getB() + 1)
                .sum();
        assertEquals(longerExpected, sum);
    }

    //TODO: add tests to verify spliterator contract


}