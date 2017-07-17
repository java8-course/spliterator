package spliterators.part2.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {


    private final OfDouble inner;
    private long currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(final long firstIndex, final OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        return inner.characteristics() | DISTINCT;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((double v) -> action.accept(new IndexedDoublePair(currentIndex++, v)));
    }

    @Override
    public Comparator<? super IndexedDoublePair> getComparator() {
        return (o1, o2) -> inner.getComparator().compare(o1.getValue(), o2.getValue());
    }

    @Override
    public void forEachRemaining(final Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((double v) -> action.accept(new IndexedDoublePair(currentIndex++, v)));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            final OfDouble ofDouble = inner.trySplit();
            if (ofDouble == null) {
                return null;
            }
            final ZipWithIndexDoubleSpliterator res = new ZipWithIndexDoubleSpliterator(currentIndex, ofDouble);
            currentIndex += estimateSize() / 2;
            return res;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
