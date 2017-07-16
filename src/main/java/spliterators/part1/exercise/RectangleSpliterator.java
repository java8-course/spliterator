package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int[][] array;
    private int startOuterIncl;
    private int endOuterExcl;

    private int innerLength;

    private int startInnerIncl;
    private int endInnerExcl;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length,
                0, 0);
        this.endInnerExcl = innerLength;
    }

    private RectangleSpliterator(int[][] array, int startOuterIncl, int endOuterExcl,
                                 int startInnerIncl, int endInnerExcl) {
        super(Long.MAX_VALUE,
                Spliterator.IMMUTABLE |
                        Spliterator.ORDERED |
                        Spliterator.SIZED |
                        Spliterator.SUBSIZED |
                        Spliterator.NONNULL);
        this.innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterIncl = startOuterIncl;
        this.endOuterExcl = endOuterExcl;

        this.startInnerIncl = startInnerIncl;
        this.endInnerExcl = endInnerExcl;
    }

    @Override
    public OfInt trySplit() {
        int rowNumber = endOuterExcl - startOuterIncl;
        int estimatedSize = (int) estimateSize();

        if (rowNumber < 2 && estimatedSize < 2) {
            return null;
        }

        int halfEstimatedSize = estimatedSize / 2;
        int halfSizeWithoutStartLine = halfEstimatedSize - (innerLength - startInnerIncl);

        int midOuterIncl = startOuterIncl;
        int midInnerIncl = startInnerIncl;
        if (halfSizeWithoutStartLine > 0) {
            int amountFullRows = halfSizeWithoutStartLine / innerLength;
            if (halfSizeWithoutStartLine % innerLength > 0) {
                midOuterIncl = midOuterIncl + amountFullRows + 1;
                midInnerIncl = halfSizeWithoutStartLine - amountFullRows*innerLength;
            } else {
                midOuterIncl += amountFullRows;
                midInnerIncl = innerLength;
            }
        } else {
            midInnerIncl += halfEstimatedSize;
        }

        final RectangleSpliterator rs = new RectangleSpliterator(array, startOuterIncl, midOuterIncl + 1,
                startInnerIncl, midInnerIncl);
        startOuterIncl = midInnerIncl == innerLength ? midOuterIncl + 1 : midOuterIncl;
        startInnerIncl = midInnerIncl == innerLength ? 0 : midInnerIncl;
        return rs;
    }

    @Override
    public long estimateSize() {
        int rowNumber = endOuterExcl - startOuterIncl;
        if (rowNumber > 2) {
            return innerLength - startInnerIncl + endInnerExcl + (rowNumber - 2) * innerLength;
        } else if (rowNumber == 2) {
            return innerLength - startInnerIncl + endInnerExcl;
        } else {
            return endInnerExcl - startInnerIncl;
        }
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterIncl == endOuterExcl - 1 && startInnerIncl >= endInnerExcl || startOuterIncl >= endOuterExcl) {
            return false;
        }

        int value = array[startOuterIncl][startInnerIncl];
        action.accept(value);

        startInnerIncl++;
        if (startInnerIncl == innerLength) {
            startInnerIncl = 0;
            startOuterIncl++;
        }

        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (int out = startOuterIncl; out < endOuterExcl; out++) {
            int endInnerLocalExcl = out == (endOuterExcl - 1)  ? endInnerExcl : innerLength;
            for (int in = startInnerIncl; in < endInnerLocalExcl; in++) {
                action.accept(array[out][in]);
            }
            startInnerIncl = 0;
        }
        startOuterIncl = endOuterExcl;
        startInnerIncl = endInnerExcl;
    }
}
