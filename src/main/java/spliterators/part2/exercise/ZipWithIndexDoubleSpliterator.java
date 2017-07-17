package spliterators.part2.exercise;

import java.util.Comparator;
import java.util.Optional;
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
        super(inner.estimateSize(), IMMUTABLE | ORDERED | SORTED | SIZED | SUBSIZED | NONNULL);
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE | ORDERED | SORTED | SIZED | SUBSIZED | NONNULL;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        if (inner.tryAdvance((double d) ->
                action.accept(new IndexedDoublePair(currentIndex, d)))) {
            currentIndex++;
            return true;
        }
        return false;
    }

    @Override
    public Comparator<? super IndexedDoublePair> getComparator() {
        return (i1, i2) -> inner.getComparator().compare(i1.getValue(), i2.getValue());
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        while (inner.tryAdvance((double d) ->
                action.accept(new IndexedDoublePair(currentIndex, d)))) {
            currentIndex++;
        }
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            return Optional.ofNullable(inner.trySplit())
                    .map(ofD -> {
                        int newCurrentIndex = currentIndex;
                        currentIndex += (int) ofD.estimateSize();
                        return new ZipWithIndexDoubleSpliterator(newCurrentIndex, ofD);})
                    .orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public long estimateSize() {
        return currentIndex + inner.estimateSize();
    }
}
