package spliterators.part2.example;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import spliterators.part2.exercise.IndexedDoublePair;
import spliterators.part2.exercise.ZipWithIndexDoubleSpliterator;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class ZipWithIndexDoubleSpliteratorTest {

    private Double[] getRandomArray(int length) {
        final Double[] result = new Double[length];

        for (int i = 0; i < length; i++) {
            result[i] = ThreadLocalRandom.current().nextDouble();
        }

        return result;
    }

    @Test
    public void comparePaired() {
        final Double[] randomArray = getRandomArray(1000);

        final List<IndexedDoublePair> result1 =
                Stream.iterate(
                        new IndexedDoublePair(0, randomArray[0]),
                        p -> new IndexedDoublePair(p.getIndex() + 1, randomArray[p.getIndex() + 1]))
                        .limit(randomArray.length)
                        .collect(toList());

        final Spliterator.OfDouble spliterator = Arrays.spliterator(ArrayUtils.toPrimitive(randomArray));
        final List<IndexedDoublePair> result2 =
        StreamSupport.stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .map(p -> new IndexedDoublePair(p.getIndex() + 1, p.getValue()))
                .map(p -> new IndexedDoublePair(p.getIndex() - 1, p.getValue()))
                .collect(toList());

        assertEquals(result1, result2);

    }
}
