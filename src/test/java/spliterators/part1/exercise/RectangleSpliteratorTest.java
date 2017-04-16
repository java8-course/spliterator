package spliterators.part1.exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RectangleSpliteratorTest {

    final static int arrayMinCount = 3;
    final static int arrayMaxCountGreatestMin = 5;
    final static int arrayInnerMinLength = 3;
    final static int arrayInnerMaxLengthExclusive = 6;

    static int[][] array;
    static int innerLength;

    static int maxLevelForSplit;
    Spliterator spliterator;

    final static Consumer consumer = v -> {
    };

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        generateArray();
        maxLevelForSplit = log2(array.length * innerLength);
    }

    private static void generateArray() {
        int arraySize = ThreadLocalRandom.current().nextInt(arrayMinCount, arrayMaxCountGreatestMin);
        innerLength = ThreadLocalRandom.current().nextInt(arrayInnerMinLength, arrayInnerMaxLengthExclusive);

        int[] ints = IntStream.iterate(1, i -> i + 1).limit(innerLength).toArray();
        array = Stream.iterate(ints, a -> generateArrayLevel2(a[0]))
                .limit(arraySize).toArray(int[][]::new);
    }

    private static int[] generateArrayLevel2(int arr) {
        return IntStream.iterate(arr + innerLength, i -> i + 1).limit(innerLength).toArray();
    }

    private static int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    private int getArrayElementCount() {
        return array.length * innerLength;
    }

    @Before
    public void setUp() throws Exception {
        spliterator = new RectangleSpliterator(array);

        System.out.print("Size: " + getArrayElementCount());
        System.out.print(". row: " + array.length + ", col: " + array[0].length + ". Cnt:\n");

        Arrays.stream(array).forEach(v -> {
            Arrays.stream(v).forEach(z -> System.out.print(z + " "));
            System.out.println();
        });

        System.out.println();
    }

    @Test
    public void tryAdvanceOneElementTest() {
        spliterator.tryAdvance(consumer);
        assertEquals(getArrayElementCount() - 1, spliterator.estimateSize());
    }

    @Test
    public void tryAdvanceOneToForEachRemTest() {
        assertEquals(true, spliterator.tryAdvance(consumer));
        assertEquals(getArrayElementCount() - 1, spliterator.estimateSize());

        spliterator.forEachRemaining(consumer);
        assertEquals(0, spliterator.estimateSize());

        assertEquals(false, spliterator.tryAdvance(consumer));
        assertEquals(0, spliterator.estimateSize());
    }

    @Test
    public void tryAdvanceToEndTest() {
        for (int i = 0; i < getArrayElementCount(); i++) {
            spliterator.tryAdvance(consumer);
        }

        assertEquals(0, spliterator.estimateSize());
        assertEquals(false, spliterator.tryAdvance(consumer));
    }

    @Test
    public void trySplitOneTest() {
        Spliterator newArrSplt = spliterator.trySplit();
        assertEquals(getArrayElementCount(), spliterator.estimateSize() + newArrSplt.estimateSize());
    }

    @Test
    public void trySplitToEndTest() {
        System.out.println("maxLevelForSplit: " + maxLevelForSplit);
        int maxSplit = trySplitToLevel(spliterator, 0);
        assertEquals(getArrayElementCount(), maxSplit);
    }

    private int trySplitToLevel(Spliterator splt, int curLevel) {
        for (int i = 0; i < curLevel; i++)
            System.out.print("--");
        System.out.println(curLevel + " before splt " + splt.estimateSize());

        Spliterator newSplt = splt.trySplit();

        if (newSplt == null) {
            return (int) splt.estimateSize();
        }

        if (curLevel > maxLevelForSplit) {
            fail();
        }

        curLevel += 1;
        int countSplt1 = trySplitToLevel(splt, curLevel);
        int countSplt2 = trySplitToLevel(newSplt, curLevel);

        return countSplt1 + countSplt2;
    }

    @Test
    public void sizeTest() {
        assertEquals(getArrayElementCount(), spliterator.estimateSize());
    }

    @After
    public void tearDown() throws Exception {
        System.out.println();
    }


    @Test
    public void usabilityTest() {
        int[][] array = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };

        int[][] array2 = {
                {1, 2, 3, 4, 5, 6, 7}
        };

        assertEquals(checkArraySum(array, false), 78);
        assertEquals(checkArraySum(array, true), 78);

        assertEquals(checkArraySum(array2, false), 28);
        assertEquals(checkArraySum(array2, true), 28);
    }

    private long checkArraySum(int[][] arrayToTest, boolean parallel) {
        long sum = StreamSupport.intStream(new RectangleSpliterator(arrayToTest), parallel)
                .asLongStream()
                .sum();

        System.out.print("Sum: " + sum + ". Parallel: " + parallel + ". Mas: ");

        RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(arrayToTest);
        System.out.print(rectangleSpliterator.tryAdvance((Consumer) val -> System.out.print(val + " ")) + " ");

        StreamSupport.intStream(rectangleSpliterator, parallel).forEach(value -> System.out.print(value + " "));
        System.out.print(rectangleSpliterator.tryAdvance((Consumer) value -> {
        }) + ". ");

        int[] ints = StreamSupport.intStream(new RectangleSpliterator(arrayToTest), parallel)
                .toArray();
        System.out.print("ArrSize: " + ints.length + ". Cnt: ");
        Arrays.stream(ints).forEach(v -> System.out.print(v + " "));

        System.out.println();

        return sum;
    }

}