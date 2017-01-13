package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int startIndex;
    private final int endIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0, array.length);
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int startIndex, int endIndex) {
        super(endIndex - startIndex, inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int characteristics() {
        return inner.characteristics() & ~Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        Consumer<A> aConsumer = (a) -> action.accept(new Pair<>(a, array[startIndex]));
        if (startIndex < endIndex && inner.tryAdvance(aConsumer)) {
            startIndex++;
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        Consumer<A> aConsumer = (a) -> {
            if (startIndex < endIndex) {
                action.accept(new Pair<>(a, array[startIndex]));
            }
        };
        inner.forEachRemaining(aConsumer);
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            Spliterator<A> innerSpliterator = inner.trySplit();
            if (innerSpliterator == null) {
                return null;
            }
            int length = endIndex - startIndex;
            if (length < 2) {
                return null;
            }
            int middle = startIndex + length / 2;
            Spliterator<Pair<A, B>> zipWithArraySpliterator =
                    new ZipWithArraySpliterator<>(innerSpliterator, array, startIndex, middle);
            startIndex += middle;
            return zipWithArraySpliterator;
        }
        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return Long.min(inner.estimateSize(),endIndex - startIndex);
    }
}
