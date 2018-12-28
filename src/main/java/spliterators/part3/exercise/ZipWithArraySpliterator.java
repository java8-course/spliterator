package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int index;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        this(inner, array, 0);
    }

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int index) {
        super(Math.min(inner.estimateSize(), array.length), inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.index = index;

    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        return inner.tryAdvance(a -> action.accept(new Pair<>(a, array[index++])));
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        while (index < estimateSize()) {
            tryAdvance(action);
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        // honestly snatched from https://github.com/sausageRoll/spliterator/blob/part3/src/main/java/spliterators/part3/exercise/ZipWithArraySpliterator.java
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            long innerSize = inner.estimateSize();
            Spliterator<A> spliterator = inner.trySplit();

            ZipWithArraySpliterator<A, B> zipWithArraySpliterator = new ZipWithArraySpliterator<>(spliterator, array, index);
            index = (int) (index + innerSize / 2);
            return zipWithArraySpliterator;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return Math.min(array.length - index, inner.estimateSize());
    }
}
