package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {

    private final Spliterator<A> inner;
    private final B[] array;
    private final AtomicLong startInclusive;
    private final long endExclusive;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(array.length, inner.characteristics());
        // TODO
        this.inner = inner;
        this.array = array;
        this.endExclusive = array.length;
        startInclusive = new AtomicLong();
    }

    private ZipWithArraySpliterator(Spliterator<A> inner, B[] array, long startInclusive, long endExclusive) {
        super(array.length, inner.characteristics());
        // TODO
        this.inner = inner;
        this.array = array;
        this.startInclusive = new AtomicLong(startInclusive);
        this.endExclusive = endExclusive;
    }

    @Override
    public int characteristics() {
        // TODO
        int characteristics = inner.characteristics();
        if (!inner.hasCharacteristics(SUBSIZED))
            throw new IllegalStateException();
        characteristics &= ~SORTED;
        return characteristics;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        // TODO
        if (startInclusive.get() >= endExclusive) return false;

        inner.tryAdvance(v -> action.accept(
                new Pair<>(v, array[(int) startInclusive.get()]))
        );
        startInclusive.incrementAndGet();
        return true;
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        // TODO
        inner.forEachRemaining(
                v ->
                        action.accept(new Pair<>(v, array[(int) startInclusive.getAndIncrement()]))
        );
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        // TODO
        final Spliterator<A> part = inner.trySplit();
        if (part != null) {
            final long prevStartInclusive = startInclusive.getAndAdd(part.estimateSize());
            return new ZipWithArraySpliterator<>(part, array, prevStartInclusive, startInclusive.get());
        } else return null;
    }

    @Override
    public long estimateSize() {
        // TODO
        return endExclusive - startInclusive.get();
    }
}
