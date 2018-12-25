package spliterators.part1.exercise;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

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
        int[][] arr = getRectangleArray(5, 10);

        RectangleSpliterator splitIterator = new RectangleSpliterator(arr);
        splitIterator.estimateSize();


    }

    @Test
    public void tryAdvance() {
        int x = 5;
        int y = 10;
        int[][] arr = getRectangleArray(5, 3);
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