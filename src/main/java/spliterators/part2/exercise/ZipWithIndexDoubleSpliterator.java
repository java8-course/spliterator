package spliterators.part2.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

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
    public Comparator<? super IndexedDoublePair> getComparator() {
        final Comparator<? super Double> innerComparator = inner.getComparator();
        if (innerComparator == null){
            return null;
        } 
        return Comparator.comparing(IndexedDoublePair::getValue, innerComparator);
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance(combineActions(action));
    }

    private Consumer<? super Double> combineActions(final Consumer<? super IndexedDoublePair> action) {
        return (Double d) -> {
            action.accept(new IndexedDoublePair(currentIndex, d));
            ++currentIndex;
        };
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining(combineActions(action));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
         if (inner.hasCharacteristics(OfDouble.SUBSIZED)) {

             final OfDouble splitted = inner.trySplit();
             if (splitted == null){
                 return null;
             }
             final ZipWithIndexDoubleSpliterator zipWithIndexDoubleSpliterator =
                     new ZipWithIndexDoubleSpliterator(currentIndex, splitted);
             currentIndex += splitted.estimateSize();
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
