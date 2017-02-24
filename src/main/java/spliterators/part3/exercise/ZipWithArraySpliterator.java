package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int index;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(inner.estimateSize(), inner.characteristics());
        this.inner = inner;
        this.array = array;
        index = 0;
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int index) {
        super(inner.estimateSize(), inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.index = index;
    }

    @Override
    public int characteristics() {
        return inner.characteristics() & ~Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return index < array.length && inner.tryAdvance(d -> {
            action.accept(new Pair<>(d, array[index]));
            index++;
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        inner.forEachRemaining(d -> {
            action.accept(new Pair<>(d, array[index]));
            index++;
        });
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            Spliterator<A> split = inner.trySplit();
            if (split == null) {
                return null;
            }
            final ZipWithArraySpliterator<A, B> zipWithArraySpliterator = new ZipWithArraySpliterator<>(split, array, index);
            index = Math.min((int) (index + split.estimateSize()), array.length);
            return zipWithArraySpliterator;
        }
        else return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), array.length - index);
    }
}
