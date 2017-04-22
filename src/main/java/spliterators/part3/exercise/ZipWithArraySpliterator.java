package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int index;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(Math.min(inner.estimateSize(), array.length), inner.characteristics());
        this.inner = inner;
        this.array = array;
        index = 0;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return index < array.length && inner.tryAdvance(a -> {
            action.accept(new Pair<>(a, array[index]));
            index++;
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        inner.forEachRemaining(a -> {
            action.accept(new Pair<>(a, array[index]));
            index++;
        });
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (!inner.hasCharacteristics(SUBSIZED)) return super.trySplit();
        Spliterator<A> split = inner.trySplit();
        if (split == null) return null;
        final ZipWithArraySpliterator<A, B> spliterator = new ZipWithArraySpliterator<>(split, array);
        spliterator.setIndex(index);
        index = Math.min((int) (index + split.estimateSize()), array.length);
        return spliterator;
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), array.length - index);
    }

    private void setIndex(int index) {
        this.index = index;
    }
}