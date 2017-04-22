package spliterators.part1.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int outerFromInclusive;
    private final int outerToExclusive;
    private int innerFromInclusive;
    private int innerToExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(int[][] array, int outerFromInclusive, int outerToExclusive, int innerFromInclusive, int innerToExclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.outerFromInclusive = outerFromInclusive;
        this.outerToExclusive = outerToExclusive;
        this.innerFromInclusive = innerFromInclusive;
        this.innerToExclusive = innerToExclusive;
    }

    @Override
    public OfInt trySplit() {
        int length = (outerToExclusive - outerFromInclusive) * innerLength - innerFromInclusive - (innerLength - innerToExclusive);
        int middleLength = length / 2;

        if (length <= 1)
            return null;

        int outerMiddle = outerFromInclusive + (innerFromInclusive + middleLength) / innerLength;
        int innerMiddle = (innerFromInclusive + middleLength) % innerLength;

        final RectangleSpliterator newSpliterator = new RectangleSpliterator(array, outerFromInclusive, outerMiddle + 1, innerFromInclusive, innerMiddle);

        outerFromInclusive = outerMiddle;
        innerFromInclusive = innerMiddle;

        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return ((long) outerToExclusive - outerFromInclusive) * innerLength - innerFromInclusive - (innerLength - innerToExclusive);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (outerFromInclusive < outerToExclusive) {
            if (outerToExclusive - outerFromInclusive == 1) {
                if (innerFromInclusive < innerToExclusive) {
                    action.accept(array[outerFromInclusive][innerFromInclusive]);
                    innerFromInclusive += 1;
                    return true;
                } else {
                    return false;
                }
            } else {
                if (innerFromInclusive < innerLength) {
                    action.accept(array[outerFromInclusive][innerFromInclusive]);
                    innerFromInclusive += 1;
                    return true;
                } else {
                    outerFromInclusive += 1;
                    innerFromInclusive = 0;
                    return tryAdvance(action);
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        int i;
        for (i = outerFromInclusive; i < outerToExclusive; i++) {
            int currentInnerFromInclusive = i == outerFromInclusive ? innerFromInclusive : 0;
            int currentInnerToExlusive = i == outerToExclusive - 1 ? innerToExclusive : innerLength;

            for (int j = currentInnerFromInclusive; j < currentInnerToExlusive; j++) {
                action.accept(array[i][j]);
                innerFromInclusive = j;
            }
        }
        outerFromInclusive = i;
    }
}
