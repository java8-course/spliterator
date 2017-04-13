package spliterators.part3.exercise;

import spliterators.part2.exercise.IndexedDoublePair;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private final int endIndex;
    private int currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0, array.length);
    }

    private ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int startIndex, int endIndex) {
        super(array.length - startIndex, inner.characteristics());
        this.inner = inner;
        this.array = array;
        currentIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return currentIndex < endIndex && inner.tryAdvance(a -> action.accept(new Pair<A, B>(a, array[currentIndex++])));
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        while (currentIndex < endIndex) {
            currentIndex++;
            if (!inner.tryAdvance(a -> action.accept(new Pair<A, B>(a, array[currentIndex])))) return;
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            Spliterator<A> splittedInner = inner.trySplit();
            currentIndex += splittedInner.estimateSize();

            return new ZipWithArraySpliterator<>(splittedInner, array,
                    (int)(currentIndex - splittedInner.estimateSize()), currentIndex);
        }

        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        if (inner.hasCharacteristics(SIZED)) return Math.min(inner.estimateSize(), endIndex - currentIndex);
        return endIndex - currentIndex;
    }
}
