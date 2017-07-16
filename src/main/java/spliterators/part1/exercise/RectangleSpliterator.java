package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int startInclusive;
    private int endExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length * array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startInclusive, int endExclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public OfInt trySplit() {
        int length = (int) estimateSize();

        if (length < 5) {
            return null;
        }

        int mid = startInclusive + length / 2;

        RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(array, startInclusive, mid);

        startInclusive = mid;
        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        return (long) (endExclusive - startInclusive);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        int i = startInclusive / innerLength;
        int j = startInclusive - i*innerLength;

        action.accept(array[i][j]);

        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (int i = startInclusive; i < endExclusive; i++) {
            int ii = i / innerLength;
            int jj = i - ii*innerLength;

            action.accept(array[ii][jj]);
        }
    }
}
