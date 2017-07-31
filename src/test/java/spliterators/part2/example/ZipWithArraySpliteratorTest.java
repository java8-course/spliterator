package spliterators.part2.example;

import org.junit.Test;
import spliterators.part1.example.ArrayExample;
import spliterators.part3.exercise.Pair;
import spliterators.part3.exercise.ZipWithArraySpliterator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

public class ZipWithArraySpliteratorTest {

    private Integer[] getRandomArray(int length) {
        final Integer[] result = new Integer[length];

        for (int i = 0; i < length; i++)
            result[i] = ThreadLocalRandom.current().nextInt();

        return result;
    }

    @Test
    public void innerIsOfGreaterLengthSuccess() {
        final Integer[] innerArray = getRandomArray(30);
        final Integer[] array = getRandomArray(10);

        final ZipWithArraySpliterator<Integer, Integer> spliterator = new ZipWithArraySpliterator<>(
                Spliterators.spliterator(innerArray, 0), array);

        final List<String> actual = StreamSupport.stream(spliterator, false)
                .map(p -> new Pair<>(p.getA().hashCode() + 1, p.getB() + 1))
                .map(Pair::toString)
                .collect(Collectors.toList());
        assertThat(actual.size(), is(10));
    }

    @Test
    public void ArrayIsOfGreaterLengthSuccess() {
        final Integer[] innerArray = getRandomArray(10);
        final Integer[] array = getRandomArray(30);

        final ZipWithArraySpliterator<Integer, Integer> spliterator = new ZipWithArraySpliterator<>(
                Spliterators.spliterator(innerArray, 0), array);

        final List<String> actual = StreamSupport.stream(spliterator, false)
                .map(p -> new Pair<>(p.getA().hashCode() + 1, p.getB() + 1))
                .map(Pair::toString)
                .collect(Collectors.toList());
        assertThat(actual.size(), is(10));
    }

    @Test
    public void nonzeroLengthSuccess() {
        final Integer[] innerArray = getRandomArray(10);
        final Integer[] array = getRandomArray(10);

        final ZipWithArraySpliterator<Integer, Integer> spliterator = new ZipWithArraySpliterator<>(
                Spliterators.spliterator(innerArray, 0), array);

        final List<String> actual = StreamSupport.stream(spliterator, false)
                .map(p -> new Pair<>(p.getA().hashCode() + 1, p.getB() + 1))
                .map(Pair::toString)
                .collect(Collectors.toList());
        assertThat(actual.size(), is(10));
    }

    @Test
    public void zeroLengthSuccess() {
        final Integer[] innerArray = getRandomArray(0);
        final Integer[] array = getRandomArray(0);

        final ZipWithArraySpliterator<Integer, Integer> spliterator = new ZipWithArraySpliterator<>(
                Spliterators.spliterator(innerArray, 0), array);

        final List<String> actual = StreamSupport.stream(spliterator, false)
                .map(p -> new Pair<>(p.getA().hashCode() + 1, p.getB() + 1))
                .map(Pair::toString)
                .collect(Collectors.toList());
        assertThat(actual.size(), is(0));
    }
}
