package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0);
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int currentIndex) {
        super(Math.min(inner.estimateSize(), array.length - currentIndex), inner.characteristics()); // FIXME:
        // TODO
        this.inner = inner;
        this.array = array;
        this.currentIndex = currentIndex;
    }

    @Override
    public int characteristics() {
        // TODO
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        // TODO
        return currentIndex < array.length && inner.tryAdvance(a -> {
            final Pair<A, B> pair = new Pair<>(a, array[currentIndex]);
            currentIndex++;
            action.accept(pair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        // TODO
        inner.forEachRemaining(a -> {
            if (currentIndex < array.length) {
                final Pair<A, B> pair = new Pair<>(a, array[currentIndex]);
                currentIndex++;
                action.accept(pair);
            }
        });
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        // TODO
        if (inner.hasCharacteristics(SUBSIZED)) {
            Spliterator<A> a = inner.trySplit();
            if (a == null) {
                return null;
            } else {
                final ZipWithArraySpliterator<A, B> spliterator = new ZipWithArraySpliterator<>(a, array, currentIndex);
                currentIndex = Math.min(currentIndex + (int) a.estimateSize(), array.length);
                return spliterator;
            }
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        // TODO
        return Math.min(inner.estimateSize(), array.length - currentIndex);
    }
}
