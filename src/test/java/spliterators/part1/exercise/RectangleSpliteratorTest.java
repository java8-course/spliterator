package spliterators.part1.exercise;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.testng.Assert.*;

/**
 * Created by Chaika Aleksei on 07.10.2016.
 */
public class RectangleSpliteratorTest {

    final int SIZE = 1000;
    int[][] crazyArray = new int[SIZE][SIZE];

    @BeforeMethod
    public void setUp() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                crazyArray[i][j] = (int) (Math.random()*1000+1);
            }
        }
    }

    @Test
    public void testForSpliterator() throws Exception {
        Stream<Integer> stream = StreamSupport.stream(new RectangleSpliterator(crazyArray), true);
        long sum = stream.mapToLong(a -> a)
                .sum();
        long expectedSum = 0l;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                expectedSum+=crazyArray[i][j];
            }
        }
        Assert.assertEquals(sum, expectedSum);
    }
}