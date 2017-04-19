package part2;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import spliterators.part2.exercise.IndexedDoublePair;
import spliterators.part2.exercise.ZipWithIndexDoubleSpliterator;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by nikita on 4/13/2017.
 */
public class ZipWithIndexDoubleSpliteratorTest {

    final double[] array = {1.4, 2.2, 4.9, 5.3, 8.7, 9.1};

    @Test
    public void testCount(){
        Spliterator.OfDouble spliterator = Arrays.spliterator(array);

        final long actual = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(spliterator), true).count();

        Assert.assertEquals(6, actual);
    }

    @Test
    public void tetsToList(){
        final List<Double> expected = Arrays.asList(1.4, 2.2, 4.9, 5.3, 8.7, 9.1);
        final List<Double> actual = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(Arrays.spliterator(array)), true)
            .map(IndexedDoublePair::getValue)
            .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void testIndexedDoublePair(){
        final List<IndexedDoublePair> expected = Arrays.asList(
            new IndexedDoublePair(0, 1.4),
            new IndexedDoublePair(1, 2.2),
            new IndexedDoublePair(2, 4.9),
            new IndexedDoublePair(3, 5.3),
            new IndexedDoublePair(4, 8.7),
            new IndexedDoublePair(5, 9.1)
        );

        List<IndexedDoublePair> actual = StreamSupport.stream(new ZipWithIndexDoubleSpliterator(Arrays.spliterator(array)), true)
            .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}
