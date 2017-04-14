package spliterators.part3.exercise;

import spliterators.part2.exercise.IndexedDoublePair;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int characteristics;
    private final int endIndexExc;
    private int currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0, array.length);
    }

    private ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int startIndex, int endIndex) {
        super(array.length - startIndex, inner.characteristics());
        this.inner = inner;
        this.array = array;
        currentIndex = startIndex;
        this.endIndexExc = endIndex;

        this.characteristics = inner.characteristics();
        if(!inner.hasCharacteristics(NONNULL)) characteristics += NONNULL;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return currentIndex < endIndexExc && inner.tryAdvance(a -> action.accept(new Pair<A, B>(a, array[currentIndex++])));
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        while (currentIndex < endIndexExc) {
            if (!inner.tryAdvance(a -> action.accept(new Pair<A, B>(a, array[currentIndex++])))) return;
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {

        Spliterator<A> splittedInner = inner.trySplit();
        if (array.length - currentIndex < 2 ||splittedInner==null) return null;

        if (inner.hasCharacteristics(SUBSIZED)) {;
            currentIndex += splittedInner.estimateSize();
            int start = (int) (currentIndex - splittedInner.estimateSize());
            int end = currentIndex < array.length ?  currentIndex: array.length;
            return new ZipWithArraySpliterator<>(splittedInner, array, start, end);
        }

        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        if (inner.hasCharacteristics(SIZED)) return Math.min(inner.estimateSize(), endIndexExc - currentIndex+1);
        return endIndexExc - currentIndex+1;
    }

    @Override
    public Comparator<Pair<A, B>> getComparator() {
        if (!inner.hasCharacteristics(SORTED)) throw new UnsupportedOperationException();
        return (p1, p2) -> inner.getComparator().compare(p1.getA(), p2.getA());
    }
}
