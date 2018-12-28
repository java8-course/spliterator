package spliterators.part3.exercise;
// honestly snatched from https://raw.githubusercontent.com/sausageRoll/spliterator/part3/src/test/java/spliterators/part3/exercise/ZipWithArraySpliteratorTest.java

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class ZipWithArraySpliteratorTest {
    private int maxSize;

    @Test
    public void testPar() {
        maxSize = 10000;

        Double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(array), Arrays.copyOf(array, array.length)), true)
                .mapToDouble(pair -> pair.getA() - pair.getB())
                .sum();

        double res = 0.0;

        assertEquals(sum, res, 0.1);
    }

    private Double[] generateDouble() {
        Double[] doubles = new Double[maxSize];

        for (int i = 0; i < maxSize; i++) {
            doubles[i] = Math.random();
        }

        return doubles;
    }

    @Test
    public void testSeq() {
        maxSize = 10000;

        Double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(array), Arrays.copyOf(array, array.length)), true)
                .mapToDouble(pair -> pair.getA() - pair.getB())
                .sum();

        double res = 0.0;

        assertEquals(sum, res, 0.1);
    }

    @Test
    public void testParDifSize() {
        maxSize = 10000;

        Double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(array), Arrays.copyOf(array, array.length/2)), true)
                .mapToDouble(pair -> pair.getA() - pair.getB())
                .sum();

        double res = 0.0;

        assertEquals(sum, res, 0.1);
    }


    @Test
    public void testSeqDifSize() {
        maxSize = 10000;

        Double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(array), Arrays.copyOf(array, array.length/2)), true)
                .mapToDouble(pair -> pair.getA() - pair.getB())
                .sum();

        double res = 0.0;

        assertEquals(sum, res, 0.1);
    }

    @Test
    public void emptyRight() {
        maxSize = 10000;

        Double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(array), Arrays.copyOf(array, 0)), true)
                .mapToDouble(pair -> pair.getA() - pair.getB())
                .sum();

        double res = 0.0;

        assertEquals(sum, res, 0.1);
    }

    @Test
    public void emptLeft() {
        maxSize = 10000;

        Double[] array = generateDouble();
        double sum = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(Arrays.copyOf(array, 0)), array), true)
                .mapToDouble(pair -> pair.getA() - pair.getB())
                .sum();

        double res = 0.0;

        assertEquals(sum, res, 0.1);
    }
}