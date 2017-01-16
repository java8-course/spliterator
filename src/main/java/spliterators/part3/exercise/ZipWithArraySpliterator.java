package spliterators.part3.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;


    private ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int startInclusive) {
        super(Math.min(inner.estimateSize(), array.length - startInclusive), inner.characteristics());
        this.inner = inner;
        this.array = array;
        currentIndex = startInclusive;
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0);
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (array.length == currentIndex) {
            return false;
        }
        return inner.tryAdvance(a -> {
            final Pair<A, B> pair = new Pair<A, B>(a, array[currentIndex]);
            currentIndex += 1;
            action.accept(pair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        if ((inner.hasCharacteristics(Spliterator.SIZED))&(inner.estimateSize() <= array.length - currentIndex)) {
            inner.forEachRemaining(a -> {
                final Pair<A, B> pair = new Pair<A, B>(a, array[currentIndex]);
                currentIndex += 1;
                action.accept(pair);
            });
        } else {
            super.forEachRemaining(action);
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            final Spliterator<A> split = inner.trySplit();
            if (split == null) return null;
            final Spliterator<Pair<A, B>> result = new ZipWithArraySpliterator<>(split, array, currentIndex);
            currentIndex = (int) Math.min(split.estimateSize() + currentIndex, array.length);
            return result;

        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Long.min(inner.estimateSize(), array.length - currentIndex);
    }


    @Override
    public Comparator<? super Pair<A, B>> getComparator() {
        return new Comparator<Pair<A, B>>() {
            @Override
            public int compare(Pair<A, B> o1, Pair<A, B> o2) {
                return o1.getA().toString().compareTo(o2.getA().toString());
            }
        };
    }
}
