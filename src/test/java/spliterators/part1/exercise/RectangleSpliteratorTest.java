package spliterators.part1.exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class RectangleSpliteratorTest {

    private int[][] array;

    @Before
    public void setup() {
        final int outerLength = 100;
        array = new int[outerLength][];

        for (int i = 0; i < array.length; i++) {
            final int innerLength = 100;
            final int[] inner = new int[innerLength];
            array[i] = inner;
            for (int j = 0; j < inner.length; j++) {
                inner[j] = ThreadLocalRandom.current().nextInt();;
            }
        }
    }

    @Test
    public void test_seq() {
        final long expected = Arrays.stream(array)
                .sequential()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
        final long res = StreamSupport.intStream(new RectangleSpliterator(array), false)
                .asLongStream()
                .sum();

        assertEquals(expected, res);
    }

    @Test
    public void test_par() {
        final long expected = Arrays.stream(array)
                .parallel()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
        final long res = StreamSupport.intStream(new RectangleSpliterator(array), true)
                .asLongStream()
                .sum();

        assertEquals(expected, res);
    }
}