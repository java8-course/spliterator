package spliterators.part3.exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class ZipWithArraySpliteratorTest {

    final private static int arrayAMinCount = 3;
    final private static int arrayAMaxCountGreatestMin = 6;
    final private static int arrayBMinCount = 5;
    final private static int arrayBMaxCountGreatestMin = 8;

    private static Integer[] arrayA;
    private static Integer[] arrayB;

    private ZipWithArraySpliterator<Integer, Integer> spliterator;
    final private Consumer<Pair> consumer = v -> System.out.println(v.getA() + " " + v.getB());

    private static int maxLevelForSplit;
    private static int minArraySize;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        int arrayASize = ThreadLocalRandom.current().nextInt(arrayAMinCount, arrayAMaxCountGreatestMin);
        int arrayBSize = ThreadLocalRandom.current().nextInt(arrayBMinCount, arrayBMaxCountGreatestMin);
        arrayA = Stream.iterate(1, i -> i + 1).limit(arrayASize).toArray(Integer[]::new);
        arrayB = Stream.iterate(arrayASize + 1, i -> i + 1).limit(arrayBSize).toArray(Integer[]::new);
        minArraySize = Math.min(arrayA.length, arrayB.length);

        maxLevelForSplit = log2(minArraySize);
    }

    @Before
    public void setUp() throws Exception {
        setUpSpliterator();

        System.out.print("ArrASize: " + arrayA.length + ". Cnt: ");
        Arrays.stream(arrayA).forEach(v -> System.out.printf("%d ", v));
        System.out.printf("\n");

        System.out.print("ArrBSize: " + arrayB.length + ". Cnt: ");
        Arrays.stream(arrayB).forEach(v -> System.out.printf("%d ", v));
        System.out.printf("\n\n");
    }

    public void setUpSpliterator() {
        spliterator = new ZipWithArraySpliterator<>(Arrays.stream(arrayA).spliterator(), arrayB);
    }

    private static int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    @Test
    public void tryAdvanceOneElementTest() {
        spliterator.tryAdvance(consumer);
        assertEquals(minArraySize - 1, spliterator.estimateSize());
    }

    @Test
    public void tryAdvanceOneToForEachRemTest() {
        assertEquals(true, spliterator.tryAdvance(consumer));
        assertEquals(minArraySize - 1, spliterator.estimateSize());

        spliterator.forEachRemaining(consumer);
        assertEquals(0, spliterator.estimateSize());

        assertEquals(false, spliterator.tryAdvance(consumer));
        assertEquals(0, spliterator.estimateSize());
    }

    @Test
    public void tryAdvanceToEndTest() {
        for (int i = 0; i < minArraySize; i++) {
            spliterator.tryAdvance(consumer);
        }

        assertEquals(0, spliterator.estimateSize());
        assertEquals(false, spliterator.tryAdvance(consumer));
    }

    @Test
    public void trySplitOneTest() {
        Spliterator newArrSplt = spliterator.trySplit();
        assertEquals(minArraySize, spliterator.estimateSize() + newArrSplt.estimateSize());
    }

    @Test
    public void trySplitToEndTest() {
        System.out.printf("maxLevelForSplit: %d\n\n", maxLevelForSplit);
        int maxSplit = trySplitToLevel(spliterator, 0);
        System.out.println();
        assertEquals(minArraySize, maxSplit);
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
        assertEquals(minArraySize, spliterator.estimateSize());
    }

    @Test
    public void usabilityTest() {
        int expectedSum = 0;
        for (int i = 0; i < minArraySize; i++) {
            System.out.printf("(%d+%d) ", arrayA[i], arrayB[i]);
            expectedSum += arrayA[i] + arrayB[i];
        }
        System.out.printf("\n\nexpectedSum: %d\n", expectedSum);

        assertEquals(expectedSum, checkArraySum(false));
        assertEquals(expectedSum, checkArraySum(true));
    }

    private int checkArraySum(boolean parallel) {
        int actualSum;

        actualSum = StreamSupport.stream(spliterator, parallel)
                .mapToInt(pair -> pair.getA() + pair.getB())
                .sum();

        System.out.printf("Parallel: %b actualSum: %d\n", parallel, actualSum);

        setUpSpliterator();

        return actualSum;
    }

    @After
    public void tearDown() throws Exception {
        System.out.println();
    }
}