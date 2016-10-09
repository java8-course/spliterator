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
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive < endOuterExclusive && startInnerInclusive < innerLength) {
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            startInnerInclusive += 1;
            if (startInnerInclusive == innerLength) {
                startInnerInclusive = 0;
                startOuterInclusive += 1;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public OfInt trySplit() {
        final int length = endOuterExclusive - startOuterInclusive;
        if (length <= 1) {
            return null;
        }

        final int middle = startOuterInclusive + length/2;
        final RectangleSpliterator newSpliterator = new RectangleSpliterator(array, startOuterInclusive, middle, 0);

        startOuterInclusive = middle;

        return newSpliterator;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for ( ; startOuterInclusive < endOuterExclusive; startOuterInclusive++) {
            for ( ; startInnerInclusive < innerLength; startInnerInclusive++) {
                action.accept(array[startOuterInclusive][startInnerInclusive]);
            }
            startInnerInclusive = 0;
        }
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

}
