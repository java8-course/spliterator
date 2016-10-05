package spliterators.part2.example;

import org.junit.Before;
import org.junit.Test;
import spliterators.part2.exercise.ZipWithIndexDoubleSpliterator;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class IndexDoubleSpliteratorTest {
    final double[] dbls = new double[100];

    @Before
    public void init() {
        for (int i = 0; i < dbls.length; i++)
            dbls[i] = ThreadLocalRandom.current().nextDouble();
    }

    @Test
    public void testSpliterator() {
        double expected = 0;
        for (int i = 0; i < dbls.length; i++)
            expected += dbls[i] * i;

        final double sum = StreamSupport.stream(
                new ZipWithIndexDoubleSpliterator(Arrays.stream(dbls).spliterator()), true)
                .mapToDouble(pair -> pair.getIndex() * pair.getValue())
                .sum();

        assertEquals(expected, sum, 0.001);
    }
}
