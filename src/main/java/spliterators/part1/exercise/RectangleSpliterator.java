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
//        throw new UnsupportedOperationException();
        long length = estimateSize();
        if (length < 2) {
            return null;
        }
        int middle = startOuterInclusive + (endOuterExclusive - startOuterInclusive) / 2;
        final OfInt splitterator = new RectangleSpliterator(array, startOuterInclusive, middle, startInnerInclusive);
        startInnerInclusive = 0;
        startOuterInclusive = middle;
        return splitterator;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
//        throw new UnsupportedOperationException();
        if (startInnerInclusive < innerLength) {
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            startInnerInclusive++;
            return true;
        } else if (startOuterInclusive < endOuterExclusive) {
            startInnerInclusive = 0;
            startOuterInclusive++;
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            return true;
        }
        return false;
    }
}
