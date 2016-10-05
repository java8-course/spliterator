package spliterators.part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

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
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        boolean didAdvance = inner.tryAdvance((double value) -> action.accept(new IndexedDoublePair(currentIndex, value)));
        if (didAdvance) currentIndex++;
        return didAdvance;
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((Double value) -> {
            action.accept(new IndexedDoublePair(currentIndex++, value));
        });
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        final Spliterator<IndexedDoublePair> newSplit;
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final OfDouble newInner = inner.trySplit();
            if (newInner != null) {
                newSplit = new ZipWithIndexDoubleSpliterator(currentIndex, newInner);
                currentIndex += (int) newInner.estimateSize();
            } else
                newSplit = null;
        } else {
            newSplit = super.trySplit();
        }
        return newSplit;
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
