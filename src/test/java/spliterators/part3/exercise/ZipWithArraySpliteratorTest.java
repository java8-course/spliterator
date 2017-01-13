package spliterators.part3.exercise;

import org.junit.Assert;
import org.junit.Test;
import spliterators.part2.example.ArrayZipWithIndexExample;
import spliterators.part2.example.IndexedArraySpliteratorTest;
import spliterators.part2.example.IndexedPair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class ZipWithArraySpliteratorTest {

    private static <A,B> Stream<Pair<A,B>> getStream(A[] values, B[] joinings, boolean parallel) {
        return StreamSupport.stream(new ZipWithArraySpliterator<>(Arrays.spliterator(values),
                joinings, 0, joinings.length), parallel);
    }

    @Test
    public void checkSpliterators() {
        final String[] randomArray = IndexedArraySpliteratorTest.getRandomArray(30);
        final Integer[] indicesArray = new Integer[20];
        for (int i = 0; i < indicesArray.length; ++i) {
            indicesArray[i] = i;
        }
        checkSpliterator(randomArray,indicesArray,true);
        checkSpliterator(randomArray,indicesArray,false);
    }

    private void checkSpliterator(String[] values, Integer[] joinings, boolean parallel){
        final long parStreamSize = getStream(values, joinings, parallel).count();
        Assert.assertTrue(parStreamSize <= joinings.length);

        final List<Pair<String, Integer>> changedList = getStream(values, joinings, parallel)
                .map(pair -> new Pair<>(pair.getA().concat("check"), pair.getB()))
                .skip(2)
                .collect(Collectors.toList());
        for (int i = 0; i < changedList.size(); ++i) {
            Assert.assertEquals(i+2, (int) changedList.get(i).getB());
            Assert.assertEquals(values[i+2].concat("check"), changedList.get(i).getA());
        }
    }
}
