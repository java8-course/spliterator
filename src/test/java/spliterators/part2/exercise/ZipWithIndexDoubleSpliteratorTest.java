package spliterators.part2.exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class ZipWithIndexDoubleSpliteratorTest {
    final private static int arrayMinCount = 5;
    final private static int arrayMaxCountGreatestMin = 15;
    private static double[] array;

    private ZipWithIndexDoubleSpliterator spliterator;
    final private Consumer consumer = v -> {
    };

    private static int maxLevelForSplit;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        int arraySize = ThreadLocalRandom.current().nextInt(arrayMinCount, arrayMaxCountGreatestMin);
        array = DoubleStream.iterate(0.5, i -> ThreadLocalRandom.current().nextDouble()).limit(arraySize).toArray();
        maxLevelForSplit = log2(array.length);
    }

    @Before
    public void setUp() throws Exception {
        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.stream(array).spliterator());

        System.out.print("ArrSize: " + array.length + ". Cnt: ");
        Arrays.stream(array).forEach(v -> System.out.printf("%.3f ", v));
        System.out.printf("\n");
    }

    private static int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }


    @Test
    public void tryAdvanceOneElementTest() {
        spliterator.tryAdvance(consumer);
        assertEquals(array.length - 1, spliterator.estimateSize());
    }

    @Test
    public void tryAdvanceOneToForEachRemTest() {
        assertEquals(true, spliterator.tryAdvance(consumer));
        assertEquals(array.length - 1, spliterator.estimateSize());

        spliterator.forEachRemaining(consumer);
        assertEquals(0, spliterator.estimateSize());

        assertEquals(false, spliterator.tryAdvance(consumer));
        assertEquals(0, spliterator.estimateSize());
    }

    @Test
    public void tryAdvanceToEndTest() {
        for (int i = 0; i < array.length; i++) {
            spliterator.tryAdvance(consumer);
        }

        assertEquals(0, spliterator.estimateSize());
        assertEquals(false, spliterator.tryAdvance(consumer));
    }

    @Test
    public void trySplitOneTest() {
        Spliterator newArrSplt = spliterator.trySplit();
        assertEquals(array.length, spliterator.estimateSize() + newArrSplt.estimateSize());
    }

    @Test
    public void trySplitToEndTest() {
        System.out.printf("\nmaxLevelForSplit: %d\n\n", maxLevelForSplit);
        int maxSplit = trySplitToLevel(spliterator, 0);
        assertEquals(array.length, maxSplit);
    }

    private int trySplitToLevel(Spliterator splt, int curLevel) {
        for (int i = 0; i < curLevel; i++) {
            System.out.print("--");
        }
        System.out.print(curLevel + " before splt " + splt.estimateSize());

        Spliterator newSplt = splt.trySplit();

        if (newSplt == null) {
            System.out.println(". NoSplit");
            return (int) splt.estimateSize();
        } else {
            System.out.println();
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
        assertEquals(array.length, spliterator.estimateSize());
    }

    @Test
    public void usabilityTest() {
        double expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            System.out.printf("(%d+%1.3f) ", i, array[i]);
            expectedSum += i + array[i];
        }
        System.out.printf("\n\nexpectedSum: %1.3f\n", expectedSum);

        assertEquals(expectedSum, checkArraySum(false), 1e-3);
        assertEquals(expectedSum, checkArraySum(true), 1e-3);
    }

    private double checkArraySum(boolean parallel) {
        double actualSum;

        actualSum = StreamSupport.stream(spliterator, parallel)
                .mapToDouble(pair -> pair.getIndex() + pair.getValue())
                .sum();

        System.out.printf("Parallel: %b actualSum: %1.3f\n", parallel, actualSum);

        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.stream(array).spliterator());

        return actualSum;
    }

    @After
    public void tearDown() throws Exception {
        System.out.println();
    }
}