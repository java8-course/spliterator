package spliterators.part1.exercise;

import org.junit.*;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

/**
 * Created by Leonid on 22.01.2017.
 */
public class RectangleSpliteratorTest {
    int[][] array;

    @Before
    public void prepare() {
        array = new int[10][];
        for (int i = 0; i < 10; i++) {
            array[i] = new int[12];
            for (int j = 0; j < 12; j++) {
                array[i][j] = 1;
            }
        }
    }

    @Test
    public void testSpliteratior() {
        Stream<Integer> stream = StreamSupport.stream(new RectangleSpliterator(array), true);
        int result1 = stream
                .mapToInt(Integer::intValue)
                .sum();
        int result2 = 0;
        for (int[] ints : array) {
            for (int anInt : ints) {
                result2 += anInt;
            }
        }
        assertEquals(result1, result2);
    }
}
