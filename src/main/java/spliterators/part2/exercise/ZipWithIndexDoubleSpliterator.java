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
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((Consumer<? super Double>) aDouble -> {
            action.accept(new IndexedDoublePair(currentIndex++, aDouble));
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((Consumer<? super Double>) aDouble -> action.accept(new IndexedDoublePair(currentIndex++,aDouble)));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // honestly snatched from https://github.com/sausageRoll/spliterator/blob/part2/src/main/java/spliterators/part2/exercise/ZipWithIndexDoubleSpliterator.java
         if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
             OfDouble ofDouble = inner.trySplit();
             currentIndex = currentIndex + (int)estimateSize() / 2;
             return new ZipWithIndexDoubleSpliterator(0, ofDouble);
         }

        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
