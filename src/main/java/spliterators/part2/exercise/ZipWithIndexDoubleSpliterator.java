package spliterators.part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {


    private final OfDouble inner;
    private /*final*/ int currentIndex;

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
        return inner.tryAdvance((DoubleConsumer) d-> {
            IndexedDoublePair indexedDoublePair = new IndexedDoublePair(currentIndex, d);
            currentIndex += 1;
            action.accept(indexedDoublePair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((double d) -> {
            IndexedDoublePair indexedDoublePair = new IndexedDoublePair(currentIndex, d);
            currentIndex += 1;
            action.accept(indexedDoublePair);
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
        // if (inner.hasCharacteristics(???)) {
        //   use inner.trySplit
        // } else
        if (inner.hasCharacteristics(SUBSIZED)){
            OfDouble zplit = inner.trySplit();

            if (zplit==null){
                return null;
            }

            ZipWithIndexDoubleSpliterator zipWithIndexDoubleSpliterator = new ZipWithIndexDoubleSpliterator(currentIndex, zplit);
            currentIndex += zplit.estimateSize();

            return zipWithIndexDoubleSpliterator;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
