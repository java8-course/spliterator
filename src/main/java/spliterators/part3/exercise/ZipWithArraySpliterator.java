package spliterators.part3.exercise;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private B[] array;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(Math.min(inner.estimateSize(), array.length), inner.characteristics());
        this.inner = inner;
        this.array = array;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return array.length != 0 && inner.tryAdvance(d -> {
            final Pair<A, B> pair = new Pair<>(d, array[0]);
            action.accept(pair);
            array = Arrays.copyOfRange(array, 1, array.length);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        if (inner.hasCharacteristics(Spliterator.SIZED) && inner.estimateSize()<=array.length) {
            inner.forEachRemaining(d -> {
                final Pair<A, B> pair = new Pair<>(d, array[0]);
                action.accept(pair);
                array = Arrays.copyOfRange(array, 1, array.length);
            });
        } else {
            super.forEachRemaining(action);
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final int arrayLength = (int) estimateSize();
            final int middle = arrayLength / 2;
            final Spliterator<A> split = inner.trySplit();
            if (split==null) {
                return null;
            }

            final ZipWithArraySpliterator<A, B> result = new ZipWithArraySpliterator<>(split, Arrays.copyOfRange(array, 0, middle));
            array = Arrays.copyOfRange(array, middle, arrayLength);
            return result;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), array.length);
    }

}
