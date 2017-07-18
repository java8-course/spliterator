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
        inner.characteristics();
        inner.hasCharacteristics(Spliterator.CONCURRENT);
        inner.hasCharacteristics(Spliterator.DISTINCT);
        inner.hasCharacteristics(Spliterator.IMMUTABLE);
        inner.hasCharacteristics(Spliterator.NONNULL);
        inner.hasCharacteristics(Spliterator.ORDERED);
        inner.hasCharacteristics(Spliterator.SIZED);
        inner.hasCharacteristics(Spliterator.SUBSIZED);
        inner.hasCharacteristics(Spliterator.SORTED);
        return inner.characteristics() | NONNULL;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        if (inner.tryAdvance((double s) -> action.accept(new IndexedDoublePair(currentIndex, s)))) {
            currentIndex++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((double s) -> {
            action.accept(new IndexedDoublePair(currentIndex, s));
            currentIndex++;
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            Spliterator<IndexedDoublePair> spliterator = new ZipWithIndexDoubleSpliterator(currentIndex, inner.trySplit());
            currentIndex += spliterator.estimateSize();
            return spliterator;
        } else{
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }

    @Override
    public Comparator<IndexedDoublePair> getComparator() {
        if (inner.hasCharacteristics(SORTED)) {
            Comparator<? super Double> comparator = inner.getComparator();
            return (o1, o2) -> comparator.compare(o1.getValue(), o2.getValue());
        } else {
            throw new IllegalStateException();
        }
    }
}
