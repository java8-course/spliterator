package spliterators.part1.exercise;


import spliterators.part1.example.ArrayExample;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private int endOuterExclusive;
    private int startInnerInclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
    }

    @Override
    public OfInt trySplit() {
        int length = endOuterExclusive - startOuterInclusive;
        if (length <= 1) {
            return null;
        }

        int middle = startOuterInclusive + length/2;

        final RectangleSpliterator newSpliterator = new RectangleSpliterator(array, startOuterInclusive, middle, startInnerInclusive);

        startOuterInclusive = middle;

        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInnerInclusive < innerLength) {
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            startInnerInclusive++;
            return true;
        }

        if (startOuterInclusive < endOuterExclusive - 1) {
            startOuterInclusive++;
            startInnerInclusive = 0;
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            startInnerInclusive++;
            return true;
        }

        return false;
    }
}
