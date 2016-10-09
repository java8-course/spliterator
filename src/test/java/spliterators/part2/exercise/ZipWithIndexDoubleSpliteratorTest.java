package spliterators.part2.exercise;

import org.junit.Assert;
import org.junit.Test;
import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.testng.Assert.*;

/**
 * Created by Chaika Aleksei on 07.10.2016.
 */
public class ZipWithIndexDoubleSpliteratorTest {

    final int SIZE = 1000*1000;
    double[] crazyArray = new double[SIZE];

    @BeforeMethod
    public void setUp() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            crazyArray[i] = (Math.random()*1000+1);
        }
    }

    @Test
    public void zipWithIndexDoubleSpliteratorTest() throws Exception{

        Stream<IndexedDoublePair> stream = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(Arrays.stream(crazyArray).spliterator()), true);
        long sum = stream
                .mapToLong(pair -> (long) pair.getValue())
                .sum();

        long expectedSum = 0l;
        for (int i = 0; i < SIZE; i++) {
                expectedSum+=((long) crazyArray[i]);
        }

        Assert.assertEquals(expectedSum,sum );
    }
}