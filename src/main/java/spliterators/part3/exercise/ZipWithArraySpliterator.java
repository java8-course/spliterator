package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {

    private final Spliterator<A> inner;
    private final B[] arrayB;
    private int currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(0, inner, array);
    }

    private ZipWithArraySpliterator(int startIndex, Spliterator<A> inner, B[] array) {
        super(Math.min(inner.estimateSize(), array.length - startIndex), inner.characteristics());
        this.inner = inner;
        this.arrayB = array;
        currentIndex = startIndex;
    }

    private long arrayRemainder() {
        return arrayB.length - currentIndex;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public long estimateSize() {
        return Math.min(inner.estimateSize(), arrayRemainder());
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (arrayRemainder() == 0) return false;

        boolean didAdvance = inner.tryAdvance(a -> action.accept(new Pair<>(a, arrayB[currentIndex])));
        if (didAdvance) currentIndex++;
        return didAdvance;
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        if ((inner.characteristics() & Spliterator.SIZED) > 0 && inner.estimateSize() <= arrayRemainder()) {
            inner.forEachRemaining(a -> action.accept(new Pair<>(a, arrayB[currentIndex++])));
        } else
            while (currentIndex < arrayB.length && inner.tryAdvance(a -> action.accept(new Pair<>(a, arrayB[currentIndex]))))
                currentIndex++;
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        final Spliterator<Pair<A, B>> newSplit;

        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final Spliterator<A> aSplit = inner.trySplit();
            if (aSplit != null) {
                newSplit = new ZipWithArraySpliterator<>(currentIndex, aSplit, arrayB);
                currentIndex = Math.min((int) (currentIndex + aSplit.estimateSize()), arrayB.length);
            } else
                newSplit = null;
        } else
            newSplit = super.trySplit();

        return newSplit;
    }

}
