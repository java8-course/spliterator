package spliterators.part1.example;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static spliterators.part1.example.ArrayExample.*;

public class ArrayExampleTest {

    final static int arrayMinCount = 6;
    final static int arrayMaxCountGreatestMin = 20;

    static int[] array;
    static int maxLevelForSplit;
    Spliterator spliterator;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        generateArray();
        maxLevelForSplit = log2(array.length);
    }

    private static void generateArray() {
        int arraySize = ThreadLocalRandom.current().nextInt(arrayMinCount, arrayMaxCountGreatestMin);

        array = IntStream.iterate(1, i -> i + 1).limit(arraySize).toArray();
    }

    private static int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    @Before
    public void setUp() throws Exception {
        spliterator = new IntArraySpliterator(array);

        System.out.print("ArrSize: " + array.length + ". Cnt: ");
        Arrays.stream(array).forEach(v -> System.out.print(v + " "));
        System.out.println();
    }

    @Test
    public void tryAdvanceOneElementTest() {
        System.out.println("---tryAdvanceOneElementTest---");

        spliterator.tryAdvance(v -> {
        });
        assertEquals(array.length - 1, spliterator.estimateSize());

        System.out.println("estimateSize: " + spliterator.estimateSize());
        System.out.println("---tryAdvanceOneElementTest---");
    }

    @Test
    public void tryAdvanceOneToForEachRemTest() {
        System.out.println("---tryAdvanceOneElementTest---");

        Consumer consumer = v -> {
        };

        assertEquals(true, spliterator.tryAdvance(consumer));
        assertEquals(array.length - 1, spliterator.estimateSize());

        spliterator.forEachRemaining(consumer);
        assertEquals(false, spliterator.tryAdvance(consumer));

        System.out.println("estimateSize: " + spliterator.estimateSize());
        System.out.println("---tryAdvanceOneElementTest---");
    }

    @Test
    public void tryAdvanceToEndTest() {
        System.out.println("---tryAdvanceToEndTest---");

        Consumer consumer = v -> {
        };

        for (int i = 0; i < array.length; i++) {
            spliterator.tryAdvance(consumer);
        }

        assertEquals(0, spliterator.estimateSize());
        assertEquals(false, spliterator.tryAdvance(consumer));

        System.out.println("estimateSize: " + spliterator.estimateSize());
        System.out.println("---tryAdvanceToEndTest---");
    }

    @Test
    public void trySplitOneTest() {
        System.out.println("---trySplitOneTest---");

        Spliterator newArrSplt = spliterator.trySplit();
        assertEquals(array.length, spliterator.estimateSize() + newArrSplt.estimateSize());

        System.out.println("Splitters size: " + spliterator.estimateSize() + " and " + newArrSplt.estimateSize());
        System.out.println("---trySplitOneTest---");
    }

    @Test
    public void trySplitToEndTest() {
        System.out.println("---trySplitToEndTest---");
        System.out.println("maxLevelForSplit: " + maxLevelForSplit);

        int maxSplit = trySplitToLevel(spliterator, 0);
        assertEquals(array.length, maxSplit);

        System.out.println("Size " + maxSplit);
        System.out.println("---trySplitToEndTest---");
    }

    private int trySplitToLevel(Spliterator splt, int curLevel) {
        for (int i = 0; i < curLevel ; i++)
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
        System.out.println("---sizeTest---");

        assertEquals(array.length, spliterator.estimateSize());

        System.out.println("array.length == arrSplit.estimateSize");
        System.out.println("---sizeTest---");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println();
    }
}