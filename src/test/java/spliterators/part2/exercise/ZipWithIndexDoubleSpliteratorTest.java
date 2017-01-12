package spliterators.part2.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

/**
 * Created by hamster on 12.01.17.
 */
public class ZipWithIndexDoubleSpliteratorTest {

    private int arrayLenght = 10;

    private double [] getRandomArray() {
        double[] randomArray = new double[arrayLenght];
        for (int i = 0; i < arrayLenght; i++) {
            randomArray[i] = ThreadLocalRandom.current().nextDouble();
        }
        return randomArray;
    }


    @Test
    public void spliteratorTest() {

        double [] array = getRandomArray();
        ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(array));

        Assert.assertEquals(spliterator.estimateSize(), arrayLenght);

        double actual = StreamSupport.stream(spliterator, true)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();

        double expected = 0;
        for (double d: array) {
            expected = expected + d;
        }

        Assert.assertEquals(expected, actual, 0.0001);

    }

}