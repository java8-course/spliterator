package spliterators.part2.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Created by student on 7/17/17.
 */
public class ZipWithIndexDoubleSpliteratorTest {

    private int maxSize;

    @Test
    public void testPar() {
        maxSize = 10000;
        Spliterator.OfDouble spliterator = DoubleStream.iterate(1.0, i -> i + 0.1)
                .limit(maxSize)
                .spliterator();


        double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(Arrays.spliterator(array)), true)
                .mapToDouble(z -> z.getIndex()*z.getValue())
                .sum();

        double res = 0.0;

        for (int i = 0; i < maxSize; ++i) {
            res += i * (array[i]);
        }

        assertEquals(sum, res, 0.1);
    }

    private double[] generateDouble() {
        double[] doubles = new double[maxSize];

        for (double aDouble : doubles) {
            aDouble = Math.random();
        }

        return doubles;
    }

    @Test
    public void testSeq() {
        maxSize = 100;
        Spliterator.OfDouble spliterator = DoubleStream.iterate(1.0, i -> i + 0.1)
                .limit(maxSize)
                .spliterator();

        double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(Arrays.spliterator(array)), false)
                .mapToDouble(z -> z.getIndex()*z.getValue())
                .sum();

        double res = 0.0;

        for (int i = 0; i < maxSize; ++i) {
            res += i * (array[i]);
        }

        assertEquals(sum, res, 0.1);
    }
}