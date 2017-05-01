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
        int outerLength = 2;
        array = new int[outerLength][];
        for (int i = 0; i < array.length; i++) {
            int innerLength = 2;
            int[] inner = new int[innerLength];
            array[i] = inner;
            for (int j = 0; j < inner.length; j++) {
                inner[j] = ThreadLocalRandom.current().nextInt();
            }
        }
    }

    @Test
    public void testIt(){
        long sum1 = Arrays.stream(array)
                .parallel()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();

        long sum = StreamSupport.intStream(new RectangleSpliterator(array), true)
                .asLongStream()
                .sum();
        assertEquals(sum1, sum);
    }
}