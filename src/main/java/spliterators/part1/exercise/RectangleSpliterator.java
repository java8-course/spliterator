package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private final int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private int endOuterExclusive;
    private int startInnerInclusive;
    private int endInnerExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(final int[][] array,
                                 final int startOuterInclusive,
                                 final int endOuterExclusive,
                                 final int startInnerInclusive,
                                 final int endInnerExclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        this.endInnerExclusive = endInnerExclusive;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
        innerLength = array.length == 0 ? 0 : array[0].length;
    }

    @Override
    public OfInt trySplit() {
        final int length = endOuterExclusive - startOuterInclusive;
        if (length < 2) {
            final int inLength = endInnerExclusive - startInnerInclusive;
            if (inLength < 5) {
                return null;
            } else {
                final int mid = startInnerInclusive + inLength / 2;
                final RectangleSpliterator res =
                        new RectangleSpliterator(array, startOuterInclusive, endOuterExclusive, mid, endInnerExclusive);
                endInnerExclusive = mid;
                return res;
            }
        }

        final int mid = startOuterInclusive + length / 2;
        final RectangleSpliterator res =
                new RectangleSpliterator(array, mid, endOuterExclusive, startInnerInclusive, endInnerExclusive);
        endOuterExclusive = mid;
        return res;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(final IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        }

        final int value = array[startOuterInclusive][startInnerInclusive];
        startInnerInclusive += 1;
        if (startInnerInclusive == endInnerExclusive) {
            startOuterInclusive++;
            startInnerInclusive = 0;
        }
        action.accept(value);
        return true;
    }
}
