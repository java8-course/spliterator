package spliterators.part2.exercise;

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
        // TODO
//        throw new UnsupportedOperationException();
        return inner.characteristics() & ~Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        // TODO
//        throw new UnsupportedOperationException();
        DoubleConsumer doubleConsumer = (value) -> action.accept(new IndexedDoublePair(currentIndex, value));
        if (inner.tryAdvance(doubleConsumer)) {
            currentIndex += 1;
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        // TODO
//        throw new UnsupportedOperationException();
        DoubleConsumer doubleConsumer = (value) -> action.accept(new IndexedDoublePair(currentIndex, value));
        inner.forEachRemaining(doubleConsumer);
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
        // if (inner.hasCharacteristics(???)) {
        //   use inner.trySplit
        // } else
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            OfDouble ofDouble = inner.trySplit();
            Spliterator<IndexedDoublePair> doublePairSpliterator = new ZipWithIndexDoubleSpliterator(currentIndex, ofDouble);
            currentIndex += estimateSize();
            return doublePairSpliterator;
        }
        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        // TODO
//        throw new UnsupportedOperationException();
        return inner.estimateSize();
    }
}
