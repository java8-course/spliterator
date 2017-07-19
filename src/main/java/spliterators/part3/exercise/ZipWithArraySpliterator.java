package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {
    private Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;

    public ZipWithArraySpliterator(final Spliterator<A> inner, final B[] array) {
        this(0, inner, array);
    }

    private ZipWithArraySpliterator(final long firstIndex, final Spliterator<A> inner, final B[] array) {
        super(Math.min(array.length - firstIndex, inner.estimateSize()), inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.currentIndex = (int) firstIndex;
    }

    @Override
    public int characteristics() {
        return inner.characteristics() & ~SORTED;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Pair<A, B>> action) {
        return inner.tryAdvance(i -> {
            action.accept(new Pair<>(i, array[currentIndex++]));
        });
    }


    @Override
    public void forEachRemaining(final Consumer<? super Pair<A, B>> action) {
        for (long i = currentIndex; i < array.length; i++) {
            inner.tryAdvance(t -> action.accept(new Pair<>(t, array[currentIndex++])));
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            final long size = estimateSize();
            final Spliterator<A> aSpliterator = inner.trySplit();
            if (aSpliterator == null) {
                return null;
            }
            final ZipWithArraySpliterator<A, B> res = new ZipWithArraySpliterator<>(currentIndex, aSpliterator, array);
            currentIndex += (int) (size / 2);
            return res;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Math.min(array.length - currentIndex, inner.estimateSize());
    }
}
