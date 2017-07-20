package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

import static java.lang.Math.abs;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private int endOuterExclusive;
    private int startInnerInclusive;
    private int endInnerExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array,
                0,
                array.length,
                0,
                array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive, int endInnerExclusive) {
        super(Long.MAX_VALUE,
                Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
        this.endInnerExclusive = endInnerExclusive;
    }


    @Override
    public OfInt trySplit() {
        int elements = abs((int) estimateSize());
        if (elements < 2) {
            return null;
        }

        int half = elements / 2;
        int midPoint = (startInnerInclusive + half) % innerLength;
        int newRows = (startInnerInclusive + half) / innerLength;
        int newOuterStart = startOuterInclusive + newRows;

        RectangleSpliterator res =
                new RectangleSpliterator(array, newOuterStart, endOuterExclusive, midPoint, endInnerExclusive);

        endInnerExclusive = midPoint;
        endOuterExclusive = midPoint == 0 ? newOuterStart : newOuterStart + 1;

        return res;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (int i = startOuterInclusive; i < endOuterExclusive; i++) {
            if (i == startOuterInclusive && i == endOuterExclusive - 1) {
                for (int j = startInnerInclusive; j < (endInnerExclusive == 0 ? innerLength : endInnerExclusive); j++) {
                    action.accept(array[i][j]);
                }
            } else if (i == endOuterExclusive - 1) {
                for (int j = 0; j < endInnerExclusive; j++) {
                    action.accept(array[i][j]);
                }
            } else if (i == startOuterInclusive) {
                for (int j = startInnerInclusive; j < innerLength; j++) {
                    action.accept(array[i][j]);
                }
            } else {
                for (int j = 0; j < innerLength; j++) {
                    action.accept(array[i][j]);
                }
            }
        }
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength
                - startInnerInclusive - (innerLength - endInnerExclusive);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        }

        final int value = array[startOuterInclusive][startInnerInclusive];
        action.accept(value);
        startInnerInclusive += 1;

        if (((endOuterExclusive - startOuterInclusive) > 1 && startInnerInclusive >= innerLength) ||
                ((endOuterExclusive - startOuterInclusive) == 1 && startInnerInclusive >= (endInnerExclusive == 0 ? innerLength : endInnerExclusive))
                ) {
            startOuterInclusive += 1;
            startInnerInclusive = 0;
        }

        return true;
    }

}
