package spliterators.part1.example;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

//https://github.com/java8-course/spliterator.git

public class ArrayExample {
    public static class IntArraySpliterator extends Spliterators.AbstractIntSpliterator {

        private final int[] array;
        private int startInclusive;
        private final int endExclusive;

        public IntArraySpliterator(int[] array) {
            this(array, 0, array.length);
        }

        private IntArraySpliterator(int[] array, int startInclusive, int endExclusive) {
            super(endExclusive - startInclusive,
                    Spliterator.IMMUTABLE
                            | Spliterator.ORDERED
                            | Spliterator.SIZED
                            | Spliterator.SUBSIZED
                            | Spliterator.NONNULL);
            this.array = array;
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (startInclusive >= endExclusive) {
                return false;
            }

            final int value = array[startInclusive];
            startInclusive += 1;
            action.accept(value);

            return true;
        }

        @Override
        public long estimateSize() {
            return endExclusive - startInclusive;
        }

        @Override
        public OfInt trySplit() {
            final int length = endExclusive - startInclusive;
            if (length < 2) {
                return null;
            }

            final int mid = startInclusive + length/2;

            final IntArraySpliterator res = new IntArraySpliterator(array, startInclusive, mid);
            startInclusive = mid;
            return res;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            for (int i = startInclusive; i < endExclusive; i++) {
                action.accept(array[i]);
            }
            startInclusive = endExclusive;
        }

        @Override
        public void forEachRemaining (Consumer<? super Integer> action) {
            for (int i = startInclusive; i < endExclusive; i++) {
                action.accept(array[i]);
            }
            startInclusive = endExclusive;
        }
    }
}
