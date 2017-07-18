package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private long currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0);
    }

    private ZipWithArraySpliterator(Spliterator<A> inner, B[] array, long startIndex){
        super(Long.MAX_VALUE, 0);
        this.array = array;
        this.inner = inner;
        currentIndex = startIndex;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (currentIndex < array.length
                && inner.tryAdvance(a -> action.accept(new Pair<>(a, array[(int) currentIndex])))) {
            currentIndex += 1;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        inner.forEachRemaining(a -> {
            if (currentIndex < array.length) {
                action.accept(new Pair<>(a, array[(int) currentIndex]));
                currentIndex += 1;
            }
        });
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        Spliterator<A> aSpliterator = inner.trySplit();
        Spliterator<Pair<A, B>> newSpliterator = new ZipWithArraySpliterator<>(aSpliterator, array, currentIndex);
        currentIndex += newSpliterator.estimateSize();
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), (array.length - currentIndex));
    }
}
