package spliterators.part2.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * 24.07.2017 by K.N.K
 */
public class ZipWithIndexDoubleSpliteratorTest {

    private double[] getRandomArray(int length) {
        final double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = ThreadLocalRandom.current().nextDouble();
        }

        return result;
    }

    @Test
    public void comparePaired() {
        final double[] randomArray = getRandomArray(1_000);

        final List<IndexedDoublePair> result1 = Stream.iterate(
                new IndexedDoublePair(0, randomArray[0]),
                p -> new IndexedDoublePair(p.getIndex() + 1, randomArray[p.getIndex() + 1]))
                .limit(randomArray.length)
                .collect(toList());

        final Spliterator.OfDouble spliterator =
                Arrays.stream(randomArray).spliterator();
        final List<IndexedDoublePair> result2 =
                StreamSupport.stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                        .map(p -> new IndexedDoublePair(p.getIndex() + 1, p.getValue()))
                        .map(p -> new IndexedDoublePair(p.getIndex() - 1, p.getValue()))
                        .collect(toList());

        assertEquals(result1, result2);
    }

    @Test
    public void testZipWithIndexSplitAndTryAdvance() throws Exception {

        double[] d = {1.1, 2.2, 3.3, 4.4, 5.5, 6.6};

        Spliterator.OfDouble spliterator = Arrays.stream(d).spliterator();

        ZipWithIndexDoubleSpliterator zip =
                new ZipWithIndexDoubleSpliterator(spliterator);

        Spliterator<IndexedDoublePair> idp1 = zip.trySplit();

        idp1.tryAdvance(pair -> {
            assertEquals(0, pair.getIndex());
            assertThat(1.1, is(pair.getValue()));
        });
        idp1.tryAdvance(pair -> {
            assertEquals(1, pair.getIndex());
            assertThat(2.2, is(pair.getValue()));
        });
        idp1.tryAdvance(pair -> {
            assertEquals(2, pair.getIndex());
            assertThat(3.3, is(pair.getValue()));
        });
        boolean b = idp1.tryAdvance(pair -> {});
        assertFalse(b);

        Spliterator<IndexedDoublePair> idp2 = zip.trySplit();
        idp2.tryAdvance(pair -> {
            assertEquals(3, pair.getIndex());
            assertThat(4.4, is(pair.getValue()));
        });
        boolean b1 = idp2.tryAdvance(pair -> {});
        assertFalse(b1);

        zip.tryAdvance(pair -> {
            assertEquals(4, pair.getIndex());
            assertThat(5.5, is(pair.getValue()));
        });
        zip.tryAdvance(pair -> {
            assertEquals(5, pair.getIndex());
            assertThat(6.6, is(pair.getValue()));
        });
        boolean b2 = zip.tryAdvance(pair -> {});
        assertFalse(b2);
    }

}