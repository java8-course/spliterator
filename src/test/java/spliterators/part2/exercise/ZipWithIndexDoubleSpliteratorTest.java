package spliterators.part2.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ZipWithIndexDoubleSpliteratorTest {
    private double[] getRandomArray(int length) {
        final double[] result = new double[length];

        for (int i = 0; i < length; i++)
            result[i] = ThreadLocalRandom.current().nextDouble();


        return result;
    }

    @Test
    public void comparePaired() {
        final double[] randomArray = getRandomArray(1000);

        final List<IndexedDoublePair> result1 =
                Stream.iterate(
                        new IndexedDoublePair(0, randomArray[0]),
                        p -> new IndexedDoublePair(p.getIndex() + 1, randomArray[p.getIndex() + 1]))
                        .limit(randomArray.length)
                        .skip(1)
                        .collect(toList());

        final List<IndexedDoublePair> result2 =
                StreamSupport.stream(
                        new ZipWithIndexDoubleSpliterator(Arrays.spliterator(randomArray)), true)
                        .skip(1)
                        .map(p -> new IndexedDoublePair(p.getIndex() + 1, p.getValue()))
                        .map(p -> new IndexedDoublePair(p.getIndex() - 1, p.getValue()))
                        .collect(toList());

        assertThat(result1, equalTo(result2));
    }

}