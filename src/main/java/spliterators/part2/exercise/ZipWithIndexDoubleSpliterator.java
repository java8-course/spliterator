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
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((DoubleConsumer) d -> {
            final IndexedDoublePair indexedDoublePair = new IndexedDoublePair(currentIndex, d);
            ++currentIndex;
            action.accept(indexedDoublePair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((DoubleConsumer) d -> {
            final IndexedDoublePair indexedDoublePair = new IndexedDoublePair(currentIndex, d);
            ++currentIndex;
            action.accept(indexedDoublePair);
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if(inner.hasCharacteristics(Spliterator.SUBSIZED)){
            final OfDouble ofDouble = inner.trySplit();
            if(ofDouble == null)
                return null;
            final ZipWithIndexDoubleSpliterator zipWithIndexDoubleSpliterator = new ZipWithIndexDoubleSpliterator(currentIndex, inner);
            currentIndex += ofDouble.estimateSize();
            return zipWithIndexDoubleSpliterator;
        }
        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
