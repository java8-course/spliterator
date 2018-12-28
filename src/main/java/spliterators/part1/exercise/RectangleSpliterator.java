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


    private int getOuterLength() {
        return endOuterExclusive - startOuterInclusive;
    }

    private int getInnerLength() {
        return innerLength - startInnerInclusive;
    }


    @Override
    public OfInt trySplit() {
        int innerSize = getInnerLength();
        int outerSize = getOuterLength();
        if(outerSize<2){
            return null;
        }

        final int mid = startOuterInclusive + outerSize/2;

        final RectangleSpliterator res = new RectangleSpliterator(array, startOuterInclusive, mid,0);
        startOuterInclusive = mid;
        return res;
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
