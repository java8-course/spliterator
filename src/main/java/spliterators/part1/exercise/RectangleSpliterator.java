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
    private int endInnerExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0,array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive,
                                 int endInnerExclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
        this.endInnerExclusive = innerLength;
        this.endInnerExclusive = endInnerExclusive;
    }

    @Override
    public OfInt trySplit() {
        final int totalLength = (int) estimateSize();
        if (totalLength < 2){
            return null;
        }
        final RectangleSpliterator result;
        int lengthLeft = totalLength / 2;
        if (lengthLeft - (innerLength - startInnerInclusive) <= 0){     // outerLength == 1
            result = new RectangleSpliterator(array,startOuterInclusive,startOuterInclusive + 1,startInnerInclusive,startInnerInclusive + lengthLeft);
            startInnerInclusive = startInnerInclusive + lengthLeft;
            return result;
        }
        lengthLeft -= (innerLength - startInnerInclusive);
        final int fullRowsCount = Math.floorDiv(lengthLeft,innerLength); // outerLength > 1
        lengthLeft -= fullRowsCount * innerLength;
        result = new RectangleSpliterator(array,startOuterInclusive,startOuterInclusive + 2 + fullRowsCount,startInnerInclusive, lengthLeft);
        startInnerInclusive = lengthLeft;
        startOuterInclusive += 1 + fullRowsCount;
        return result;
    }

    @Override
    public long estimateSize() {
        int outerLength = endOuterExclusive - startOuterInclusive;
        return innerLength*(outerLength - 1) + endInnerExclusive - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInnerInclusive >= innerLength){
            startInnerInclusive = 0;
            ++startOuterInclusive;
        }
        if (startOuterInclusive >= endOuterExclusive){
            return false;
        }
        if (endOuterExclusive == startOuterInclusive + 1 && startInnerInclusive >= endInnerExclusive) {
            return false;
        }
        action.accept(array[startOuterInclusive][startInnerInclusive]);
        ++startInnerInclusive;
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        int outer = startOuterInclusive;
        int inner = startInnerInclusive;
        for (; outer < endOuterExclusive - 1; ++outer){
            for (; inner < innerLength; ++inner){
                action.accept(array[outer][inner]);
            }
            inner = 0;
        }
        if (endOuterExclusive == outer + 1){
            for (; inner < endInnerExclusive; ++inner){
                action.accept(array[outer][inner]);
            }
        }
    }
}
