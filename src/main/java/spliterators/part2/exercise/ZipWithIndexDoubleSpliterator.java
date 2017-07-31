package spliterators.part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {


    private final OfDouble inner;
    private AtomicLong currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(long firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        if (! inner.hasCharacteristics(SUBSIZED)) throw new IllegalStateException("Zip got not subsized Spliterator");
        currentIndex = new AtomicLong(firstIndex);
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        int characteristics = inner.characteristics();
        characteristics &= ~SORTED;
        return characteristics;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        // TODO
        final boolean res =
                inner.tryAdvance((Double v) ->
                        action.accept(new IndexedDoublePair((int)currentIndex.get(), v)));
        if (res) currentIndex.incrementAndGet();
        return res;
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        // TODO
        inner.forEachRemaining((Double v) -> {
            action.accept(new IndexedDoublePair((int)currentIndex.get(), v));
            currentIndex.incrementAndGet();
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
         if (inner.hasCharacteristics(SUBSIZED)) {
             OfDouble newSplit = inner.trySplit();
             if (newSplit == null)
                 return null;
             Spliterator<IndexedDoublePair> zipped =
                     new ZipWithIndexDoubleSpliterator(currentIndex.get(), newSplit);
             currentIndex.addAndGet(newSplit.estimateSize());
             return zipped;
        } else return super.trySplit();
    }

    @Override
    public long estimateSize() {
        // TODO
        return inner.estimateSize();
    }
}
