package spliterators.part3.exercise;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;


public class ZipWithArraySpliteratorTest {

    private List<Integer> listWithLessSize = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
    private List<Integer> listWithSameSize = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    private List<Integer> listWithMoreSize = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));

    private Integer[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void testCount() {
        final long expectedLess = listWithLessSize.size();
        final long actualLess = StreamSupport
                .stream(new ZipWithArraySpliterator<>(listWithLessSize.spliterator(), array), true)
                .count();
        assertEquals(expectedLess, actualLess);

        final long expectedSame = listWithSameSize.size();
        final long actualSame = StreamSupport
                .stream(new ZipWithArraySpliterator<>(listWithSameSize.spliterator(), array), true)
                .count();
        assertEquals(expectedSame, actualSame);

        final long expectedMore = array.length;
        final long actualMore = StreamSupport
                .stream(new ZipWithArraySpliterator<>(listWithMoreSize.spliterator(), array), true)
                .count();
        assertEquals(expectedMore, actualMore);
    }

    @Test
    public void testSkip() {
        long actual = StreamSupport
                .stream(new ZipWithArraySpliterator<>(listWithSameSize.spliterator(), array), true)
                .skip(2)
                .mapToInt(Pair::getA)
                .sum();
        assertEquals(44, actual);
    }

    @Test
    public void testLimit() {
        long actual = StreamSupport
                .stream(new ZipWithArraySpliterator<>(listWithSameSize.spliterator(), array), true)
                .limit(4)
                .mapToInt(Pair::getA)
                .sum();
        assertEquals(6, actual);
    }
}