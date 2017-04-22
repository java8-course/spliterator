/**
 * Created by Aleksandra_Pankratova on 22-Apr-17.
 */
package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class RectangleSpliteratorTest {
    @Test
    public void spliteratorTest() {
        int[][] array = {{12, 21, 37, 49, 53}, {61, 72, 81, 99, 100}, {43, 53, 66, 74, 89}};
        long actual = StreamSupport
                .intStream(new RectangleSpliterator(array), true)
                .asLongStream()
                .sum();
        long expected = Arrays
                .stream(array)
                .parallel()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
        Assert.assertEquals(expected, actual);
    }
}