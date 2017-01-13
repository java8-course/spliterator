package spliterators.part3.exercise;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int startInclusive;
    private int endExclusive;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array, int startInclusive,
                                   int endExclusive) {
        super(inner.estimateSize(), inner.characteristics());
        this.array = array;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public Comparator<? super Pair<A, B>> getComparator() {
        final Comparator<? super A> innerComparator = inner.getComparator();
        return innerComparator == null ? null : Comparator.comparing(Pair::getA,innerComparator);
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        if (startInclusive >= endExclusive || startInclusive >= array.length){
            return false;
        }
        return inner.tryAdvance(combineActions(action));
    }

    private Consumer<? super A> combineActions(Consumer<? super Pair<A, B>> action) {
        return a -> {
            action.accept(new Pair<>(a, array[startInclusive]));
            ++startInclusive;
        };
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        while (tryAdvance(action));
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SUBSIZED)) {
            final Spliterator<A> splitted = inner.trySplit();
            if (splitted == null){
                return null;
            }
            int resultEndExclusive = startInclusive + (int)splitted.estimateSize();
            if (resultEndExclusive > array.length){
                resultEndExclusive = array.length;
            }
            final ZipWithArraySpliterator<A,B> resultSpliterator =
                    new ZipWithArraySpliterator<>(splitted, array, startInclusive, resultEndExclusive);
            startInclusive = resultEndExclusive;
            return resultSpliterator;
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
