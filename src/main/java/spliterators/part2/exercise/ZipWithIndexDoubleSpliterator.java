package spliterators.part2.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {

    private final OfDouble inner;
    private int currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance(convert(action));
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining(convert(action));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            OfDouble newSplit = inner.trySplit();
            if (newSplit == null) {
                return null;
            }
            Spliterator<IndexedDoublePair> zipped =
                    new ZipWithIndexDoubleSpliterator(currentIndex, newSplit);
            currentIndex += newSplit.estimateSize();
            return zipped;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.getExactSizeIfKnown();
    }

    @Override
    public Comparator<? super IndexedDoublePair> getComparator() {
        Comparator<? super Double> comparator = inner.getComparator();
        return comparator == null ? null : Comparator.comparing(IndexedDoublePair::getValue, comparator);
    }

    private DoubleConsumer convert(Consumer<? super IndexedDoublePair> action) {
        return value -> action.accept(new IndexedDoublePair(currentIndex++, value));
    }
}
