package spliterators.part1.exercise;


import spliterators.part1.example.ArrayExample;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private final int endOuterExclusive;
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


    private int getInnerLength() {
        return endOuterExclusive - startOuterInclusive;
    }

    private int getOuterLength() {
        return innerLength - startInnerInclusive;
    }


    @Override
    public OfInt trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if(startInnerInclusive >= innerLength){
            startOuterInclusive++;
            startInnerInclusive = 0;
        }

        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        }

        final int value = array[startOuterInclusive][startInnerInclusive];
        ++startInnerInclusive;
        action.accept(value);

        return true;
    }
}
