package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int current;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0);
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int start) {

        super(Math.min(inner.estimateSize(), array.length - start), inner.characteristics());
        this.array = array;
        this.inner = inner;
        this.current = start;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (current < array.length)
            return false;
        return inner.tryAdvance(
                c -> {
                    final Pair<A, B> pair = new Pair<>(c, array[current]);
                    current += 1;
                    action.accept(pair);
                });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        if (inner.hasCharacteristics(SIZED) && inner.estimateSize() <= array.length - current) {
            inner.forEachRemaining(
                    c -> {
                        final Pair<A, B> pair = new Pair<>(c, array[current]);
                        current += 1;
                        action.accept(pair);
                    });
        } else {
            super.forEachRemaining(action);
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            final Spliterator<A> split = inner.trySplit();
            if (split == null) return null;
            final Spliterator<Pair<A, B>> spliterator = new ZipWithArraySpliterator<>(split, array, current);
            current = (int) Math.min(split.estimateSize() + current, array.length);
            return spliterator;
        } else return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), array.length - current);
    }
}
