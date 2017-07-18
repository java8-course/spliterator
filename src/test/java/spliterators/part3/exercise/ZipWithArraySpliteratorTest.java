package spliterators.part3.exercise;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class ZipWithArraySpliteratorTest {

    private Integer[] array = {1, 2, 3};
    private Spliterator<Integer> arraySpltr = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6}).spliterator();
    private ZipWithArraySpliterator<Integer, Integer> zipSpltr = new ZipWithArraySpliterator<>(arraySpltr, array);

    @Test
    public void testEstimatingSizeForFirstCreating() {
        int expected = 6;
        int actual = (int) zipSpltr.estimateSize();
        assertThat(actual, is(expected));
    }

    @Test
    public void testFirstlySplitting() {
        Spliterator<Pair<Integer, Integer>> firstSpl = zipSpltr.trySplit();
        Spliterator<Pair<Integer, Integer>> secondSpl = zipSpltr;

        assertThat((int) firstSpl.estimateSize(), is(3));
        assertThat((int) secondSpl.estimateSize(), is(6));

        List<Pair<Integer, Integer>> expectedFirstListOfSpl = Arrays.asList(
                new Pair<>(1, 1),
                new Pair<>(2, 2),
                new Pair<>(3, 3));
        List<Pair<Integer, Integer>> actualFirstListOfSpl = new ArrayList<>();
        while (firstSpl.tryAdvance(actualFirstListOfSpl::add)) ;
        assertEquals(expectedFirstListOfSpl, actualFirstListOfSpl);

        List<Pair<Integer, Integer>> expectedSecondListOfSpl = Collections.emptyList();
        List<Pair<Integer, Integer>> actualSecondListOfSpl = new ArrayList<>();
        while (secondSpl.tryAdvance(actualSecondListOfSpl::add)) ;
        assertEquals(expectedSecondListOfSpl, actualSecondListOfSpl);
    }
}