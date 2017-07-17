package spliterators.part2.exercise;

import org.junit.Test;

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
        maxSize = 100;
        Spliterator.OfDouble spliterator = DoubleStream.iterate(1.0, i -> i + 0.1)
                .limit(maxSize)
                .spliterator();

        double sum = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .mapToDouble(z -> z.getIndex()*z.getValue())
                .sum();

        double res = 0.0;

        for (int i = 0; i < maxSize; ++i) {
            res += i * (1.0 + 0.1*i);
        }

        assertEquals(sum, res, 0.1);
    }

    @Test
    public void testSeq() {
        maxSize = 100;
        Spliterator.OfDouble spliterator = DoubleStream.iterate(1.0, i -> i + 0.1)
                .limit(maxSize)
                .spliterator();

        double sum = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(spliterator), false)
                .mapToDouble(z -> z.getIndex()*z.getValue())
                .sum();

        double res = 0.0;

        for (int i = 0; i < maxSize; ++i) {
            res += i * (1.0 + 0.1*i);
        }

        assertEquals(sum, res, 0.1);
    }
}