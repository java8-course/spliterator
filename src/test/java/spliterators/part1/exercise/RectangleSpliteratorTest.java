package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class RectangleSpliteratorTest {
    @Test
    public void spliteratorTest() {
        int [][] array = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10}
        };
        long actual = StreamSupport.intStream(new RectangleSpliterator(array), true)
                .asLongStream()
                .sum();
        long expected = Arrays.stream(array)
                .parallel()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
        Assert.assertEquals(expected, actual);
    }
}
