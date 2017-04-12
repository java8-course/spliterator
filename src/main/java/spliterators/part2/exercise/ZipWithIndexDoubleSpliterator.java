package spliterators.part2.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {


    private final OfDouble inner;
    private int currentIndex;
    private int characteristics;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
        characteristics = inner.characteristics();

        if(!inner.hasCharacteristics(NONNULL))  characteristics += NONNULL;
        if(!inner.hasCharacteristics(SORTED))  characteristics += SORTED;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((Double d) -> action.accept(new IndexedDoublePair(currentIndex++, d)));
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((Double d) -> action.accept(new IndexedDoublePair(currentIndex++, d)));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {

        if (inner.hasCharacteristics(SUBSIZED)) {
            ZipWithIndexDoubleSpliterator res = new ZipWithIndexDoubleSpliterator(inner.trySplit());
            currentIndex+=res.estimateSize();
            return res;
        } else
            return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }

    @Override
    public Comparator<IndexedDoublePair> getComparator() {
        return Comparator.comparing(IndexedDoublePair::getIndex);
    }
}
