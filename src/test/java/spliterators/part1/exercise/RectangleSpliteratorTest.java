package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class RectangleSpliteratorTest {
    @Test
    public void spliteratorTest() {
        int [][] array = { {12, 31, 17, 29, 43}, {51, 72, 91, 89, 100}, {43, 23, 26, 34, 29} };
        long actual = StreamSupport.intStream(new RectangleSpliterator(array), true).asLongStream().sum();
        long expected = Arrays.stream(array).parallel().flatMapToInt(Arrays::stream).asLongStream().sum();
        Assert.assertEquals(expected, actual);
    }
}
