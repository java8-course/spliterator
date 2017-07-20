package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int innerLength;
    private int[][] array;
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
        // TODO
        final int outerLen = endOuterExclusive - startOuterInclusive;
        if (outerLen <= 1) return null;

        final int outerMid = startOuterInclusive + outerLen/2;

        final RectangleSpliterator res = new RectangleSpliterator(array, startOuterInclusive, outerMid, startInnerInclusive);
        startOuterInclusive = outerMid;
        return res;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive
                && startInnerInclusive >= innerLength)
            return false;

        for (int i = startInnerInclusive; i < innerLength; i++) {
            final int value = array[startOuterInclusive][startInnerInclusive];
            startInnerInclusive++;
            action.accept(value);
        }

        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        // TODO
        for (int i = startOuterInclusive; i < endOuterExclusive; i++)
            for (int j = startInnerInclusive; j < innerLength; j++)
                action.accept(array[i][j]);

        startOuterInclusive = endOuterExclusive;
    }
}
