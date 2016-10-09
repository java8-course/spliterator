package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {

    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(0, inner, array);
    }

    public ZipWithArraySpliterator(int firstIndex, Spliterator<A> inner, B[] array) {
        super(Math.min(inner.estimateSize(), array.length - firstIndex),
                inner.characteristics());

        currentIndex = firstIndex;
        this.inner = inner;
        this.array = array;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (array.length - currentIndex == 0) return false;

        boolean innerTryAdvance = inner.tryAdvance(a -> {
            final Pair<A, B> pair = new Pair<>(a, array[currentIndex]);
            action.accept(pair);
        });

        if (innerTryAdvance) {
            currentIndex += 1;
        }

        return innerTryAdvance;
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        if (inner.hasCharacteristics(SIZED) && inner.estimateSize() <= array.length - currentIndex) {
            inner.forEachRemaining(a -> {
                Pair<A, B> pair = new Pair<>(a, array[currentIndex]);
                currentIndex += 1;
                action.accept(pair);
            });
        } else
            super.forEachRemaining(action);
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        final Spliterator<Pair<A, B>> result;

        if (inner.hasCharacteristics(SUBSIZED)) {
            final Spliterator<A> split = inner.trySplit();
            if (split != null) {
                result = new ZipWithArraySpliterator<>(currentIndex, split, array);
                currentIndex = Math.min((int) (currentIndex + split.estimateSize()), array.length);
            } else
                result = null;
        } else
            result = super.trySplit();

        return result;
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), array.length - currentIndex);
    }
}
