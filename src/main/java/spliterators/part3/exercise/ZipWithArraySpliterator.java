package spliterators.part3.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;
    private int currentIndex=0;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(0, inner.characteristics());
        this.inner = inner;
        this.array = array;

    }


    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action){
        return inner.tryAdvance(d-> {
            Pair<A,B> pair = new Pair(currentIndex, d);
            currentIndex += 1;
            action.accept(pair);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        inner.forEachRemaining(d->{
            Pair<A,B> pair = new Pair(currentIndex, d);
            currentIndex += 1;
            action.accept(pair);
        });
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        if (inner.hasCharacteristics(SIZED)){
            Spliterator split = inner.trySplit();
            if (split==null) return null;
            else {
                ZipWithArraySpliterator zipWithArraySpliterator = new ZipWithArraySpliterator<>(split, array);
                currentIndex+=split.estimateSize();
                return zipWithArraySpliterator;
            }
        } else return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return
                inner.estimateSize()<(array.length-currentIndex)?
                inner.estimateSize():array.length-currentIndex;
    }
}
