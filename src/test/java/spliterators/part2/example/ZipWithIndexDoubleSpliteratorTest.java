package spliterators.part2.example;

import org.junit.Test;
import spliterators.part2.exercise.ZipWithIndexDoubleSpliterator;
import spliterators.part3.exercise.Pair;

import java.util.List;
import java.util.Spliterators;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ZipWithIndexDoubleSpliteratorTest {
    private double[] getRandomArray(int length) {
        final double[] result = new double[length];

        for (int i = 0; i < length; i++)
            result[i] = ThreadLocalRandom.current().nextInt();

        return result;
    }

    @Test
    public void nonzeroLengthSuccess() {
        final double[] randomArray = getRandomArray(10);

        final List<String> collect = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(
                Spliterators.spliterator(randomArray, 0)), true)
                .map(p -> new Pair<>(p.getIndex() + 1, p.getValue() + 1))
                .map(Pair::toString)
                .collect(Collectors.toList());
        assertThat(collect.size(), is(10));
    }

    @Test
    public void zeroLengthSuccess() {
        final double[] randomArray = getRandomArray(0);

        final List<String> collect = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(
                Spliterators.spliterator(randomArray, 0)), true)
                .map(p -> new Pair<>(p.getIndex() + 1, p.getValue() + 1))
                .map(Pair::toString)
                .collect(Collectors.toList());
        assertThat(collect.size(), is(0));
    }
}
