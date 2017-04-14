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
        return inner.characteristics()
                | NONNULL;
    }

    @Override
    public Comparator<? super IndexedDoublePair> getComparator() {
        if (inner.hasCharacteristics(SORTED)) {
            return Comparator.comparing(IndexedDoublePair::getValue);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        boolean res = inner.tryAdvance((Double e) -> action.accept(new IndexedDoublePair(currentIndex, e)));
        currentIndex += 1;
        return res;
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((Double e) -> {
            action.accept(new IndexedDoublePair(currentIndex, e));
            currentIndex += 1;
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            OfDouble splitInner = this.inner.trySplit();
            ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(currentIndex, splitInner);
            currentIndex += splitInner.estimateSize();
            return spliterator;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
