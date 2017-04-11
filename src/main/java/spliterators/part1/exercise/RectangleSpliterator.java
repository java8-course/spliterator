package spliterators.part1.exercise;


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

    @Override
    public OfInt trySplit() {
        final int length = endOuterExclusive - startOuterInclusive;

        if (length < 2) {
            return null;
        }

        int middle = startOuterInclusive + length/2;
        final RectangleSpliterator res = new RectangleSpliterator(array, startOuterInclusive, middle, startInnerInclusive);
        startOuterInclusive = middle;
        startInnerInclusive = 0;
        return res;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        }
        if (startInnerInclusive >= innerLength) {
            if (startOuterInclusive < endOuterExclusive -1) {
                startInnerInclusive = 0;
                ++startOuterInclusive;
            } else {
                return false;
            }
        }
        action.accept(array[startOuterInclusive][startInnerInclusive]);
        ++startInnerInclusive;
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (int i = startOuterInclusive; i < endOuterExclusive; i++) {
            for (int j = startInnerInclusive; j < innerLength; j++) {
                action.accept(array[i][j]);
            }
            startInnerInclusive = 0;
        }
        startOuterInclusive = endOuterExclusive;
    }
}
