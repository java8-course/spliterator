package spliterators.part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

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
        // TODO
        return inner.characteristics() & ~Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        // TODO
        return inner.tryAdvance((DoubleConsumer) d -> {
            final IndexedDoublePair pair = new IndexedDoublePair(currentIndex, d);
            currentIndex++;
            action.accept(pair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        // TODO
        inner.forEachRemaining((DoubleConsumer) d -> {
            final IndexedDoublePair pair = new IndexedDoublePair(currentIndex, d);
            currentIndex++;
            action.accept(pair);
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final OfDouble split = inner.trySplit();
            if (split == null) {
                return null;
            } else {
                return new ZipWithIndexDoubleSpliterator(currentIndex, split);
            }
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        // TODO
        return inner.estimateSize();
    }
}
