package spliterators.part3.exercise;

import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {

    private final Spliterator<A> inner;
    private int currentIndex;

    private final B[] array;
    private int arrayStartIndex;
    private int arrayEndIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(0, inner, 0, 0, array);

    }

    private ZipWithArraySpliterator(int currentIndex, Spliterator<A> inner,
                                    int arrayStartIndex, int arrayEndIndex, B[] array) {
        super(inner.estimateSize(), ORDERED | SIZED | SUBSIZED | NONNULL | CONCURRENT);
        this.inner = inner;
        this.array = array;
        this.currentIndex = currentIndex;
        this.arrayStartIndex = arrayStartIndex;
        this.arrayEndIndex = arrayEndIndex;
    }

    @Override
    public int characteristics() {
        return ORDERED | SIZED | SUBSIZED | NONNULL | CONCURRENT;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (currentIndex < array.length &&
                inner.tryAdvance((a) -> action.accept(new Pair<>(a, array[currentIndex])))) {
            currentIndex++;
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        while (currentIndex < array.length &&
                inner.tryAdvance(a -> action.accept(new Pair<>(a, array[currentIndex])))) {
            currentIndex++;
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            return Optional.ofNullable(inner.trySplit())
                    .map(newInner -> {
                        int newCurrentIndex = currentIndex;
                        currentIndex += (int) newInner.estimateSize();

                        arrayStartIndex = newCurrentIndex < array.length ? newCurrentIndex : array.length - 1;
                        arrayEndIndex = currentIndex < array.length ? currentIndex : array.length - 1;

                        Spliterator<Pair<A, B>> spliterator = new ZipWithArraySpliterator(newCurrentIndex, newInner,
                                arrayStartIndex, arrayEndIndex, array);

                        arrayStartIndex = currentIndex < array.length ? currentIndex : array.length - 1;
                        long newSizeOfSpltr = spliterator.estimateSize();
                        arrayEndIndex = newSizeOfSpltr < array.length ? (int) newSizeOfSpltr : array.length - 1;

                        return spliterator;
                    })
                    .orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public long estimateSize() {
        return currentIndex + inner.estimateSize();
    }
}

