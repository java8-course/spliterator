package spliterators.part2.exercise;

import org.junit.Test;
import spliterators.part1.exercise.RectangleSpliterator;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class ZipWithIndexDoubleSpliteratorTest {

    @Test
    public void testByStream() {
        double[] data = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));

        double res1 = StreamSupport.stream(spliterator, true)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();
        assertThat(res1, is(21.0));

        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
        double res2 = StreamSupport.stream(spliterator, true)
                .skip(3)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();
        assertThat(res2, is(15.0));

        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
        double res3 = StreamSupport.stream(spliterator, true)
                .mapToDouble(IndexedDoublePair::getValue)
                .reduce((e1, e2) -> e1 * e2)
                .getAsDouble();
        assertThat(res3, is(720.0));
    }

    @Test
    public void testCount() {
        double[] data = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
        long res4 = StreamSupport.stream(spliterator, true)
                .count();
        assertThat(res4, is(10L));

        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
        long res5 = StreamSupport.stream(spliterator, true)
                .skip(9)
                .count();
        assertThat(res5, is(1L));
    }

}