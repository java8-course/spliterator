package spliterators.part2.example;

import org.junit.Assert;
import org.junit.Test;
import spliterators.part2.exercise.IndexedDoublePair;
import spliterators.part2.exercise.ZipWithIndexDoubleSpliterator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ZipWithIndexSpliteratorTest {

    @Test
    public void checkSpliterator(){
        final double[] doubles = {6.5, 3.4, 8.3, 9.1, 0.5, 0.6};

        final ZipWithIndexDoubleSpliterator spliterator= new ZipWithIndexDoubleSpliterator(Arrays.spliterator(doubles));
        Assert.assertEquals(doubles.length,spliterator.estimateSize());

        final List<IndexedDoublePair> expectedlyChanged = new ArrayList<>();
        for (int i = 0; i < doubles.length; ++i){
            if (i % 2 == 1){
                expectedlyChanged.add(new IndexedDoublePair(i,doubles[i]*-1));
            } else{
                expectedlyChanged.add(new IndexedDoublePair(i,0.0));
            }
        }
        final List<IndexedDoublePair> actuallyChanged = StreamSupport.stream(spliterator, false)
                .map(pair -> {
                    double value = 0.0;
                    if (pair.getIndex() % 2 == 1) {
                        value = pair.getValue() * -1;
                    }
                    return new IndexedDoublePair(pair.getIndex(), value);
                })
                .collect(Collectors.toList());
        Assert.assertEquals(expectedlyChanged,actuallyChanged);
    }
}
