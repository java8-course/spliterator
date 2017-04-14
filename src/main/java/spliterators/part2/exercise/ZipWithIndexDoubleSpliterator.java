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

    public ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
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
        DoubleConsumer doubleConsumer = (t) -> action.accept(new IndexedDoublePair(currentIndex++, t));
        return inner.tryAdvance(doubleConsumer);
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((Double a) -> action.accept(new IndexedDoublePair(currentIndex++, a)));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            return new ZipWithIndexDoubleSpliterator(currentIndex++, inner.trySplit());
        } else
            return super.trySplit();
    }

    @Override
    public long estimateSize() {
      return currentIndex;
    }
}
