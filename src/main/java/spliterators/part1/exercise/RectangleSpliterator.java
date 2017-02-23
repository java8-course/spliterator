package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
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
        int length = endOuterExclusive - startOuterInclusive;
        if(length <= 1) return null;
        int middle = startOuterInclusive + length / 2;
        RectangleSpliterator rectangleSpliterator =
                new RectangleSpliterator(array, startOuterInclusive, middle, startInnerInclusive);
        startOuterInclusive = middle;
        startInnerInclusive = 0;
        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInnerInclusive < array[0].length) {
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            startInnerInclusive++;
            if (startInnerInclusive == array[0].length && startOuterInclusive < endOuterExclusive) {
                startInnerInclusive = 0;
                startOuterInclusive++;
            }
            return true;
        } else return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super Integer> action) {
        while (startOuterInclusive < endOuterExclusive) {
            while (startInnerInclusive < array[0].length) {
                action.accept(array[startOuterInclusive][startInnerInclusive]);
                startInnerInclusive++;
            }
            startOuterInclusive++;
            startInnerInclusive = 0;
        }
    }
}
