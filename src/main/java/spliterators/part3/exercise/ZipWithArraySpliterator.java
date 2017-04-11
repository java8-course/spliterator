package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;
    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(inner.estimateSize(), inner.characteristics()); // FIXME:
        this.inner = inner;
        this.array = array;
        currentIndex = 0;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return currentIndex <= array.length && inner.tryAdvance(c -> {
            final Pair<A, B> pair = new Pair<>(c, array[currentIndex]);
            currentIndex++;
            action.accept(pair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        inner.forEachRemaining( a -> {
            final Pair<A, B> pair = new Pair<>(a, array[currentIndex]);
            currentIndex++;
            action.accept(pair);
        });
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            final Spliterator<A> aSpliterator = inner.trySplit();
            if (aSpliterator == null) return null;
            final ZipWithArraySpliterator<A, B> abZipWithArraySpliterator = new ZipWithArraySpliterator<>(aSpliterator, array);
            currentIndex += aSpliterator.estimateSize();
            return abZipWithArraySpliterator;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Long.min(inner.estimateSize(), array.length - currentIndex);
    }
}
