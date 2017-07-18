package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private Spliterator<A> inner;
    int currentIndex;
    private final B[] array;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0);
    }

    private ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int index) {
        super(Math.min(inner.estimateSize(), array.length), inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.currentIndex = index;
    }

    @Override
    public int characteristics() {
        return inner.characteristics() & ~Spliterator.SORTED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return inner.tryAdvance(value ->
                action.accept(
                        new Pair<>(value, array[currentIndex++])
                )
        );
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        for (; currentIndex < estimateSize(); ++currentIndex) {
            inner.tryAdvance(value -> new Pair<>(value, array[currentIndex]));
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            long innerSize = inner.estimateSize();
            Spliterator<A> aSpliterator = inner.trySplit();

            ZipWithArraySpliterator<A, B> abZipWithArraySpliterator = new ZipWithArraySpliterator<>(aSpliterator, array, currentIndex);
            currentIndex = (int) (currentIndex + innerSize / 2);
            return  abZipWithArraySpliterator;
        } else {
            //return null;
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Math.min(array.length - currentIndex, inner.estimateSize());
    }
}
