package spliterators.part1.exercise;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RectangleSpliteratorTest {


    private int[][] getRectangleArray(int x, int y) {
        int[][] arr = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                arr[i][j] = ThreadLocalRandom.current().nextInt();
            }
        }
        return arr;
    }

    @Test
    public void trySplit() {
        int x = 5;
        int y = 10;
        int[][] arr = getRectangleArray(x, y);

        final long[] expected = {0};
        traverse(arr,x,y,v-> expected[0] +=v);

        long sum1 = StreamSupport.intStream(new RectangleSpliterator(arr), false)
                .asLongStream()
                .sum();

        assertEquals(expected[0],sum1);

        long sum2 = StreamSupport.intStream(new RectangleSpliterator(arr), true)
                .asLongStream()
                .sum();
        assertEquals(expected[0],sum2);
    }

    @Test
    public void tryAdvance() {
        int x = 5;
        int y = 10;
        int[][] arr = getRectangleArray(x, y);
        RectangleSpliterator splitIterator = new RectangleSpliterator(arr);
        traverse(arr, x, y, integer -> {
            splitIterator.tryAdvance((IntConsumer) expectable -> {
                        assertEquals((long) integer, (long) expectable);
                    }
            );
        });
    }

    private void traverse(int[][] arr, int x, int y, Consumer<Integer> f) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                f.accept(arr[i][j]);
            }
        }
    }
}