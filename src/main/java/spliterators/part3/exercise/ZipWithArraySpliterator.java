package spliterators.part3.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {

    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;
    private int endIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0, array.length);
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int startIndex, int endIndex) {
        super(array.length, inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.currentIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int characteristics() {
        return inner.characteristics()
                | NONNULL
                | SIZED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return (currentIndex < endIndex
            && inner.tryAdvance(a -> action.accept(new Pair<A, B>(a, array[currentIndex++]))));
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        while (currentIndex < endIndex) {
            if (!inner.tryAdvance(a -> action.accept(new Pair<A, B>(a, array[currentIndex++])))) {
                return;
            }
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        Spliterator<A> splitInner = this.inner.trySplit();
        if (splitInner == null || array.length - currentIndex <= 1) {
            return null;
        }

        if (inner.hasCharacteristics(SUBSIZED)) {
            int newStart = currentIndex;
            currentIndex += splitInner.estimateSize();
            int newEnd = currentIndex < array.length ? currentIndex : array.length;

            return new ZipWithArraySpliterator<>(splitInner, array, newStart, newEnd);
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return endIndex - currentIndex;
    }

    @Override
    public Comparator<Pair<A, B>> getComparator() {
        if (inner.hasCharacteristics(SORTED)) {
            return (p1, p2) -> inner.getComparator().compare(p1.getA(), p2.getA());
        } else {
            throw new IllegalStateException();
        }
    }
}
