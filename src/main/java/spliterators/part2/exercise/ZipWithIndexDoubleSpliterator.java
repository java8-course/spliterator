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

    private ZipWithIndexDoubleSpliterator(long firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        return inner.characteristics() | Spliterator.DISTINCT /*& ~Spliterator.SORTED*/;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((double d) -> action.accept(new IndexedDoublePair(currentIndex++, d)));
    }

    @Override
    public Comparator<? super IndexedDoublePair> getComparator() {
        return (i1, i2) -> inner.getComparator().compare(i1.getValue(),i2.getValue());
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining(
                (double d) -> action.accept(new IndexedDoublePair(currentIndex++, d))
        );
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            OfDouble ofDouble = inner.trySplit();
            currentIndex = currentIndex + estimateSize() / 2;
            return new ZipWithIndexDoubleSpliterator(0, ofDouble);
        } else {
            return super.trySplit();
        }

    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
