package spliterators.part2.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ZipWithIndexSpliteratorTest {
    @Test
    public void checkSpliterator(){
        final double[] array = {3.4, 2.17, 3.33, 9.0, 1.44};

        final ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(array));
        Assert.assertEquals(array.length,spliterator.estimateSize());

        final List<IndexedDoublePair> expected = new ArrayList<>();
        for (int i = 0; i < array.length; ++i){
            if (i % 2 == 1){
                expected.add(new IndexedDoublePair(i,array[i]*-1));
            } else{
                expected.add(new IndexedDoublePair(i,0.0));
            }
        }
        final List<IndexedDoublePair> actual = StreamSupport.stream(spliterator, false)
                .map(pair -> {
                    double value = 0.0;
                    if (pair.getIndex() % 2 == 1) {
                        value = pair.getValue() * -1;
                    }
                    return new IndexedDoublePair(pair.getIndex(), value);
                })
                .collect(Collectors.toList());
        Assert.assertEquals(expected, actual);
    }
}