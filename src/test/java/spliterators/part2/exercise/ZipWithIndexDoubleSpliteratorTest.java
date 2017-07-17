package spliterators.part2.exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class ZipWithIndexDoubleSpliteratorTest {

    private int[] array;
    private final static int MAX_SIZE = 1000;
    private List<IndexedDoublePair> res;


    @Before
    public void setup() {
        array = new int[MAX_SIZE];
        res = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
            res.add(new IndexedDoublePair(i, array[i]));
        }
    }

    public Stream<IndexedDoublePair> getMyStream(final boolean isParallel) {
        final Spliterator.OfDouble spliterator = Arrays
                .stream(array)
                .asDoubleStream()
                .spliterator();
        return StreamSupport
                .stream(new ZipWithIndexDoubleSpliterator(spliterator), isParallel);
    }

    @Test
    public void testSeq() {
        final Object collect = getMyStream(false)
                .collect(Collectors.toList());
        assertEquals(res, collect);
    }

    @Test
    public void testPar() {
        final Object collect = getMyStream(true)
                .collect(Collectors.toList());
        assertEquals(res, collect);
    }
}