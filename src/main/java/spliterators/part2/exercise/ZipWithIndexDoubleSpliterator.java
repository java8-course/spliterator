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
        return inner.characteristics() & -Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        // TODO
//        throw new UnsupportedOperationException();
        return inner.tryAdvance((DoubleConsumer) d -> {
            final IndexedDoublePair newPair = new IndexedDoublePair(currentIndex, d);
            currentIndex += 1;
            action.accept(newPair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        // TODO
//        throw new UnsupportedOperationException();
        inner.forEachRemaining((DoubleConsumer) d -> {
            final IndexedDoublePair newPair = new IndexedDoublePair(currentIndex, d);
            currentIndex += 1;
            action.accept(newPair);
        });
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
        // if (inner.hasCharacteristics(???)) {
        //   use inner.trySplit
        // } else

//        return super.trySplit();

        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final OfDouble split = this.inner.trySplit();
            if (split == null){
                return null;
            }
            return new ZipWithIndexDoubleSpliterator(currentIndex, split);
        }
        else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        // TODO
//        throw new UnsupportedOperationException();
        return inner.estimateSize();
    }
}
