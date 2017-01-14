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
        final int length = endOuterExclusive - startOuterInclusive;
        if (length <= 0){
            return null;
        }
        if (length == 1){
            return tryInnerSplit();
        }
        final int middle = startOuterInclusive + length /2;
        RectangleSpliterator result = new RectangleSpliterator(array,startOuterInclusive,middle,startInnerInclusive,
                endInnerExclusive);
        startOuterInclusive = middle;
        return result;
    }

    private OfInt tryInnerSplit() {
        final int length = endInnerExclusive - startInnerInclusive;
        if (length < 2){
            return null;
        }
        final int middle = startInnerInclusive + length / 2;
        RectangleSpliterator result = new RectangleSpliterator(array,startOuterInclusive,endOuterExclusive,
                startInnerInclusive,middle);
        startInnerInclusive = middle;
        return result;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*(endInnerExclusive - startInnerInclusive);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive){
            return false;
        }
        if (startInnerInclusive >= endInnerExclusive) {
            ++startOuterInclusive;
            if (startOuterInclusive >= endOuterExclusive){
                return false;
            }
            startInnerInclusive = 0;
            endInnerExclusive = array[startOuterInclusive].length;
        }
        action.accept(array[startOuterInclusive][startInnerInclusive]);
        ++startInnerInclusive;
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while(tryAdvance(action));
    }
}
