package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
//        super(Long.MAX_VALUE, 0); // FIXME:
        // TODO
//        throw new UnsupportedOperationException();
        this(inner,array,0);
    }

    public ZipWithArraySpliterator (Spliterator<A> inner, B[] array, int currentIndex) {
        super(Math.min(inner.estimateSize(), array.length - currentIndex), inner.characteristics());
        this.inner = inner;
        this.array = array;
        this.currentIndex = currentIndex;
    }

    @Override
    public int characteristics() {
        // TODO
//        throw new UnsupportedOperationException();
        return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        // TODO
//        throw new UnsupportedOperationException();
        if (currentIndex <= array.length){
            return false;
        }
        return inner.tryAdvance( a -> {
            final Pair<A,B> newPair = new Pair<>(a, array[currentIndex]);
            currentIndex += 1;
            action.accept(newPair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        // TODO
//        throw new UnsupportedOperationException();
        if (inner.hasCharacteristics(SIZED) && inner.estimateSize() <= array.length - currentIndex){
            inner.forEachRemaining( a -> {
                Pair<A,B> newPair = new Pair<A, B>(a,  array[currentIndex]);
                currentIndex += 1;
                action.accept(newPair);
            });
        }
        else {
            super.forEachRemaining(action);
        }
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        // TODO
//        throw new UnsupportedOperationException();
        if (inner.hasCharacteristics(SUBSIZED)){
            final Spliterator<A> spliterator = inner.trySplit();
            if (spliterator == null){
                return null;
            }
            final ZipWithArraySpliterator newSpliterator = new ZipWithArraySpliterator(spliterator,array,currentIndex);
            currentIndex = Math.min((int) (currentIndex + spliterator.estimateSize()), array.length);
            return newSpliterator;
        }
        else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        // TODO
//        throw new UnsupportedOperationException();
        return Math.min((int) (inner.estimateSize()), array.length - currentIndex);
    }
}
