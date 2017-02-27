package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int index;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(Math.min(inner.estimateSize(),array.length), inner.characteristics());
        this.inner = inner;
        this.array = array;
        index = 0;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return index < array.length && inner.tryAdvance(a -> {
            Pair<A, B> pair = new Pair<>(a, array[index]);
            index = index + 1;
            action.accept(pair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        if (inner.hasCharacteristics(SIZED) && inner.estimateSize() <= array.length - index) {
            inner.forEachRemaining(a -> {
                Pair<A, B> pair = new Pair<>(a, array[index]);
                index = index + 1;
                action.accept(pair);
            });
        } else {
            super.forEachRemaining(action);
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            Spliterator<A> split = inner.trySplit();
            if (split == null) return null;
            final ZipWithArraySpliterator<A, B> spliterator = new ZipWithArraySpliterator<>(split, array);
            spliterator.setIndex(index);
            index = Math.min((int) (index + split.estimateSize()), array.length);
            return spliterator;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), array.length - index);
    }
}
