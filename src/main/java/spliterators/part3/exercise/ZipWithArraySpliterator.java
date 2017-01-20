package spliterators.part3.exercise;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {
    private final Spliterator<A> inner;
    private final B[] array;
    private int origin;
    private final int fence;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(Math.min(inner.estimateSize(), array.length), inner.characteristics()
                | SORTED
                | ORDERED);
        this.inner = inner;
        this.array = array;
        origin = 0;
        fence = ((int) Math.min(inner.estimateSize(), array.length));
    }

    @Override
    public int characteristics() {
        return inner.characteristics() | SORTED | ORDERED;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (origin < fence)
            return inner.tryAdvance(p -> {
                action.accept(new Pair<>(p, array[origin]));
                ++origin;
            });
        else
            return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        for (B b : array) {
            inner.tryAdvance(p -> {
                action.accept(new Pair<>(p, array[origin]));
                ++origin;
            });
        }
        origin = fence;
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SUBSIZED)) {
            final Spliterator<A> spliteratorA = inner.trySplit();
            if (spliteratorA == null)
                return null;
            if (origin < fence) {
                return new ZipWithArraySpliterator<>(inner.trySplit(), Arrays.copyOfRange(array, array.length / 2, array.length));
            } else
                return null;
        } else
            return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return fence - origin;
    }
}
