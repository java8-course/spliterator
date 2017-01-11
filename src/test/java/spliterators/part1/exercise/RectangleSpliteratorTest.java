package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * Created by hamster on 11.01.17.
 */
public class RectangleSpliteratorTest {

    @Test
    public void spliteratorTest() {
        int [][] array = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15}
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