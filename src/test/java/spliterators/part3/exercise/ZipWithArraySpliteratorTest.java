package spliterators.part3.exercise;

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

import static junit.framework.TestCase.assertEquals;

public class ZipWithArraySpliteratorTest {
    private Integer[] array, index;
    private final static int MAX_SIZE = 10000;
    private final static int INDEX_SIZE = 10000;

    private List<Pair> res;


    @Before
    public void setup() {
        array = new Integer[MAX_SIZE];
        index = new Integer[INDEX_SIZE];
        res = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
            index[i] = i;
            res.add(new Pair<>(array[i], index[i]));
        }
    }

    private Stream<Pair<Integer, Integer>> getMyStream(final boolean isParallel, final int rightIndex, final int rightSpliterator) {
        final Spliterator<Integer> spliterator = Arrays
                .spliterator(Arrays.copyOf(array, rightSpliterator));
        return StreamSupport.stream(new ZipWithArraySpliterator<>(spliterator, Arrays.copyOf(index, rightIndex)), isParallel);
    }

    @Test
    public void testSeq() {
        final Object collect = getMyStream(false, INDEX_SIZE, MAX_SIZE)
                .collect(Collectors.toList());
        assertEquals(res, collect);
    }

    @Test
    public void testPar() {
        final Object collect = getMyStream(true, INDEX_SIZE, MAX_SIZE)
                .collect(Collectors.toList());
        assertEquals(res, collect);
    }

    @Test
    public void testSeqSub() {
        final int offset = 5000;
        final Object collect = getMyStream(false, INDEX_SIZE - offset, MAX_SIZE)
                .collect(Collectors.toList());
        assertEquals(res.subList(0, INDEX_SIZE - offset) , collect);
    }

    @Test
    public void testParSub() {
        final int offset = 5000;
        final Object collect = getMyStream(true, INDEX_SIZE, MAX_SIZE - offset)
                .collect(Collectors.toList());
        assertEquals(res.subList(0, MAX_SIZE - offset), collect);
    }

}