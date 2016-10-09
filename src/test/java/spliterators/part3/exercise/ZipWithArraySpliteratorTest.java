package spliterators.part3.exercise;

import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.*;

/**
 * Created by Chaika Aleksei on 07.10.2016.
 */
public class ZipWithArraySpliteratorTest {

    final int SIZE = 1;
    Integer[] crazyArray;
    Integer[] anotherArray;

    @BeforeMethod
    public void setUp() throws Exception {
        crazyArray = new Integer[SIZE];
        anotherArray  = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) {
            crazyArray[i] = (int) (Math.random()*1000+1);
            anotherArray[i] = (int) (Math.random()*1000+1);
        }
    }

    @Test
    public void zipWithArraySpliteratorTest() throws Exception{

        setUp();

        Stream<Pair<Integer, Integer>> stream = StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.stream(crazyArray).spliterator(), anotherArray), true);

        long sumForeach=0l;
        for (int i = 0; i < SIZE; i++) {
            sumForeach+=(crazyArray[i]+anotherArray[i]);
        }

        long sum = stream
                .mapToLong(pair -> pair.getA() + pair.getB())
                .sum();

        Assert.assertEquals(sumForeach, sum);   //TEST DOESNT PASS((((
    }
}