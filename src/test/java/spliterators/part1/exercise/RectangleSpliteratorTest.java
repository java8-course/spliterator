package spliterators.part1.exercise;

import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Created by student on 7/14/17.
 */

public class RectangleSpliteratorTest {

    public int outerLength;

    public int innerLength;

    public int[][] array;

    @Before
    public void setup() {
        outerLength = ThreadLocalRandom.current().nextInt(1000);
        innerLength = ThreadLocalRandom.current().nextInt(1000);


        array = new int[outerLength][];

        for (int i = 0; i < array.length; i++) {
            int[] inner = new int[innerLength];
            array[i] = inner;
            for (int j = 0; j < inner.length; j++) {
                inner[j] = ThreadLocalRandom.current().nextInt();
            }
        }
    }

    @Test
    public void sum() {
        long l = rectangle_par();
        long l1 = rectangle_seq();

        long res = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                res += array[i][j];
            }
        }

        assertEquals(res,l);
        assertEquals(res,l1);
    }

    public long rectangle_seq() {
        final boolean parallel = false;
        return StreamSupport.intStream(new RectangleSpliterator(array), parallel)
                .asLongStream()
                .sum();
    }

    public long rectangle_par() {
        final boolean parallel = true;
        return StreamSupport.intStream(new RectangleSpliterator(array), parallel)
                .asLongStream()
                .sum();
    }

}