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
    public int characteristics() {
        return inner.characteristics() & ~Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((Double d) -> {
            action.accept(new IndexedDoublePair(currentIndex, d));
            currentIndex++;
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((Double d) -> {
            action.accept(new IndexedDoublePair(currentIndex, d));
            currentIndex++;
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final OfDouble ofDouble = inner.trySplit();
            if(ofDouble == null) return null;
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
