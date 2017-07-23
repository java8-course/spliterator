package spliterators.part2.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * 24.07.2017 by K.N.K
 */
public class ZipWithIndexDoubleSpliteratorTest {

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