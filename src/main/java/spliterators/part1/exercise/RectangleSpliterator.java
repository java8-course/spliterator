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
        final int outerLength = endOuterExclusive = startOuterInclusive;
        if (outerLength <= 1) {
            return null;
        }
        final int outerMiddle = startOuterInclusive + outerLength / 2;

        final RectangleSpliterator result = new RectangleSpliterator(array, startOuterInclusive, outerMiddle, startInnerInclusive);
        startOuterInclusive = outerMiddle;
        return result;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive && startInnerInclusive <= innerLength) {
            return false;
        }
        for (int i = startInnerInclusive; i < innerLength; i++) {
            int value = array[startInnerInclusive][startInnerInclusive];
            startInnerInclusive++;
            action.accept(value);
        }
        return true;
    }
}
