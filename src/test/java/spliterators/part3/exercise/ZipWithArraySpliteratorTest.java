package spliterators.part3.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Created by hamster on 13.01.17.
 */
public class ZipWithArraySpliteratorTest {

    private int arrayLenght = 10;
    private int streamLenght = 15;

    private Integer [] getRandomArray() {
        Integer [] randomArray = new Integer[arrayLenght];
        for (int i = 0; i < arrayLenght; i++) {
            randomArray[i] = ThreadLocalRandom.current().nextInt();
        }
        return randomArray;
    }

    private List<Integer> getRandomList() {
        List<Integer> list = new ArrayList<>(streamLenght);
        for (int i = 0; i < streamLenght; i++) {
            list.add(ThreadLocalRandom.current().nextInt());
        }
        return list;
    }

    @Test
    public void estimateSizeTest() {
        ZipWithArraySpliterator zipWithArraySpliterator = new ZipWithArraySpliterator(getRandomList().spliterator(), getRandomArray());
        Assert.assertEquals(Integer.min(arrayLenght, streamLenght), zipWithArraySpliterator.estimateSize());
    }

    @Test
    public void sumTest() {
        Integer [] array = getRandomArray();
        List<Integer> list = getRandomList();
        int expected = 0;
        for (int i = 0; i < Math.min(arrayLenght, streamLenght); i++) {
            expected = expected + array[i];
        }
        expected = expected + list.stream()
                .limit(Math.min(arrayLenght, streamLenght))
                .mapToInt(i -> i).sum();

        Stream<Pair> streamFromSpliterator = StreamSupport.stream(new ZipWithArraySpliterator(list.spliterator(), array), true);

        int actual = streamFromSpliterator.mapToInt(p -> (int) p.getA() + (int) p.getB()).sum();

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void skipTest() {
        Integer [] array = getRandomArray();
        List<Integer> list = getRandomList();
        Stream<Pair> streamFromSpliterator = StreamSupport.stream(new ZipWithArraySpliterator(list.spliterator(), array), true);

        Pair pair = new Pair(list.get(0), array[0]);

        Assert.assertEquals(pair, streamFromSpliterator.skip(0).toArray()[0]);



    }

    @Test
    public void anyMatchTest() {
        Integer [] array = getRandomArray();
        List<Integer> list = getRandomList();


        for (int i = 0; i < 10; i++) {
            Pair pair = new Pair(list.get(i), array[i]);
            Stream<Pair> streamFromSpliterator = StreamSupport.stream(new ZipWithArraySpliterator(list.spliterator(), array), true);
            Assert.assertTrue(streamFromSpliterator.anyMatch(p -> p.equals(pair)));
        }

    }

}