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
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
       DoubleConsumer consumer = value -> action.accept(new IndexedDoublePair(currentIndex, value));
        if(inner.tryAdvance(consumer)){
            currentIndex++;
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        DoubleConsumer consumer = value -> action.accept(new IndexedDoublePair(currentIndex,value));
        inner.forEachRemaining(consumer);
        currentIndex++;
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {

        if(inner.hasCharacteristics(Spliterator.SUBSIZED)){
            OfDouble ofDouble = inner.trySplit();
            if(ofDouble == null){
                return null;
            }
            final ZipWithIndexDoubleSpliterator zipWithIndexDoubleSpliterator =
                new ZipWithIndexDoubleSpliterator(currentIndex, ofDouble);
            currentIndex += ofDouble.estimateSize();
            return zipWithIndexDoubleSpliterator;
        }
        else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
