package spliterators.part1.exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RectangleSpliteratorTest {

    private static RectangleSpliterator testedMain;
    private int[][] testedArr = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

    private List<Integer> expectedFirstListOfSpl = Arrays.asList(1, 2, 3, 4);
    private List<Integer> expectedSecondListOfSpl = Arrays.asList(5, 6, 7, 8, 9);

    private List<Integer> expectedFirstListOfSpliterator = Arrays.asList(1, 2);
    private List<Integer> expectedSecondListOfSpliterator = Arrays.asList(3, 4);

    private List<Integer> expectedThirdListOfSpliterator = Arrays.asList(5, 6);
    private List<Integer> expectedFourthListOfSpliterator = Arrays.asList(7, 8, 9);

    @Before
    public void initSpliterator() {
        testedMain = new RectangleSpliterator(testedArr);
    }

    @Test
    public void testEstimatingSizeForFirstCreating() {
        int expected = 9;
        int actual = (int) testedMain.estimateSize();
        assertThat(actual, is(expected));
    }

    @Test
    public void testFirstlySplittingOfEvenArrayRowsNumber() {
        int[][] testedArr = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        testedMain = new RectangleSpliterator(testedArr);

        Spliterator.OfInt firstSpl = testedMain.trySplit();
        Spliterator.OfInt secondSpl = testedMain;

        assertThat((int) firstSpl.estimateSize(), is(6));
        assertThat((int) secondSpl.estimateSize(), is(6));

        List<Integer> expectedFirstListOfSpl = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> actualFirstListOfSpl = new ArrayList<>();
        while (firstSpl.tryAdvance((Consumer<? super Integer>) actualFirstListOfSpl::add)) ;
        assertEquals(expectedFirstListOfSpl, actualFirstListOfSpl);

        List<Integer> expectedSecondListOfSpl = Arrays.asList(7, 8, 9, 10, 11, 12);
        List<Integer> actualSecondListOfSpl = new ArrayList<>();
        while (secondSpl.tryAdvance((Consumer<? super Integer>) actualSecondListOfSpl::add)) ;
        assertEquals(expectedSecondListOfSpl, actualSecondListOfSpl);
    }


    @Test
    public void testFirstlySplittingAndForEachRemaining() {
        Spliterator.OfInt firstSpl = testedMain.trySplit();
        Spliterator.OfInt secondSpl = testedMain;

        assertThat((int) secondSpl.estimateSize(), is(5));
        assertThat((int) firstSpl.estimateSize(), is(4));

        List<Integer> actualFirstListOfSpl = new ArrayList<>();
        firstSpl.forEachRemaining((Consumer<? super Integer>) actualFirstListOfSpl::add);
        assertEquals(expectedFirstListOfSpl, actualFirstListOfSpl);

        List<Integer> actualSecondListOfSpl = new ArrayList<>();
        secondSpl.forEachRemaining((Consumer<? super Integer>) actualSecondListOfSpl::add);
        assertEquals(expectedSecondListOfSpl, actualSecondListOfSpl);
    }

    @Test
    public void testSecondlySplittingAndForEachRemaining() {
        Spliterator.OfInt splitMain = testedMain.trySplit();

        Spliterator.OfInt firstSpl = splitMain.trySplit();
        Spliterator.OfInt secondSpl = splitMain;
        Spliterator.OfInt thirdSpl = testedMain.trySplit();
        Spliterator.OfInt fourthSpl = testedMain;

        assertThat((int) firstSpl.estimateSize(), is(2));
        assertThat((int) secondSpl.estimateSize(), is(2));
        assertThat((int) thirdSpl.estimateSize(), is(2));
        assertThat((int) fourthSpl.estimateSize(), is(3));

        List<Integer> actualFirstListOfSpl = new ArrayList<>();
        firstSpl.forEachRemaining((Consumer<? super Integer>) actualFirstListOfSpl::add);
        assertEquals(expectedFirstListOfSpliterator, actualFirstListOfSpl);

        List<Integer> actualSecondListOfSpl = new ArrayList<>();
        secondSpl.forEachRemaining((Consumer<? super Integer>) actualSecondListOfSpl::add);
        assertEquals(expectedSecondListOfSpliterator, actualSecondListOfSpl);

        List<Integer> actualThirdListOfSpl = new ArrayList<>();
        thirdSpl.forEachRemaining((Consumer<? super Integer>) actualThirdListOfSpl::add);
        assertEquals(expectedThirdListOfSpliterator, actualThirdListOfSpl);

        List<Integer> actualFourthListOfSpl = new ArrayList<>();
        fourthSpl.forEachRemaining((Consumer<? super Integer>) actualFourthListOfSpl::add);
        assertEquals(expectedFourthListOfSpliterator, actualFourthListOfSpl);
    }

    @Test
    public void testThirdlySplitting() {
        Spliterator.OfInt splitMain = testedMain.trySplit();

        Spliterator.OfInt firstSpliteratorOfSeconlySplitting = splitMain.trySplit();
        Spliterator.OfInt secondSpliteratorOfSeconlySplitting = splitMain;
        Spliterator.OfInt thirdSpliteratorOfSeconlySplitting = testedMain.trySplit();
        Spliterator.OfInt fourthSpliteratorOfSeconlySplitting = testedMain;

        Spliterator.OfInt firstSpliteratorOfThirdlySplitting = firstSpliteratorOfSeconlySplitting.trySplit();
        Spliterator.OfInt secondSpliteratorOfThirdlySplitting = firstSpliteratorOfSeconlySplitting;
        Spliterator.OfInt thirdSpliteratorOfThirdlySplitting = secondSpliteratorOfSeconlySplitting.trySplit();
        Spliterator.OfInt fourthSpliteratorOfThirdlySplitting = secondSpliteratorOfSeconlySplitting;
        Spliterator.OfInt fifthSpliteratorOfThirdlySplitting = thirdSpliteratorOfSeconlySplitting.trySplit();
        Spliterator.OfInt sixthSpliteratorOfThirdlySplitting = thirdSpliteratorOfSeconlySplitting;
        Spliterator.OfInt seventhSpliteratorOfThirdlySplitting = fourthSpliteratorOfSeconlySplitting.trySplit();
        Spliterator.OfInt eighthSpliteratorOfThirdlySplitting = fourthSpliteratorOfSeconlySplitting;

        assertThat((int) firstSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) secondSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) thirdSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) fourthSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) fifthSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) sixthSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) seventhSpliteratorOfThirdlySplitting.estimateSize(), is(1));
        assertThat((int) eighthSpliteratorOfThirdlySplitting.estimateSize(), is(2));

    }

    @Test
    public void testFourthlySplitting() {
        Spliterator.OfInt splitMain = testedMain.trySplit();
        Spliterator.OfInt firstSpliteratorOfSeconlySplitting = splitMain.trySplit();
        Spliterator.OfInt firstSpliteratorOfThirdlySplitting = firstSpliteratorOfSeconlySplitting.trySplit();

        assertThat((int) firstSpliteratorOfThirdlySplitting.estimateSize(), is(1));

        Spliterator.OfInt firstSpliteratorOfFourthlySplitting = firstSpliteratorOfThirdlySplitting.trySplit();

        assertNull(firstSpliteratorOfFourthlySplitting);
    }

    @Test
    public void testTryAdvanceAfterFirstSplitting() {
        Spliterator.OfInt firstSpl = testedMain.trySplit();
        Spliterator.OfInt secondSpl = testedMain;

        assertThat((int) secondSpl.estimateSize(), is(5));
        assertThat((int) firstSpl.estimateSize(), is(4));

        List<Integer> actualFirstListOfSpl = new ArrayList<>();
        while (firstSpl.tryAdvance((Consumer<? super Integer>) actualFirstListOfSpl::add)) ;
        assertEquals(expectedFirstListOfSpl, actualFirstListOfSpl);

        List<Integer> actualSecondListOfSpl = new ArrayList<>();
        while (secondSpl.tryAdvance((Consumer<? super Integer>) actualSecondListOfSpl::add)) ;
        assertEquals(expectedSecondListOfSpl, actualSecondListOfSpl);
    }
}