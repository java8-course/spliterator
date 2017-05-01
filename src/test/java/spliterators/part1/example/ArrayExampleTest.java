package spliterators.part1.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class ArrayExampleTest {
    @Test
    public void IntArraySpliterator() {
        int[] array = new int[] {1, 2, 3, 4, 5};
        final boolean parallel = false;
        long res = StreamSupport.intStream(new ArrayExample.IntArraySpliterator(array), parallel)
                .asLongStream()
                .sum();
        Assert.assertEquals(15L, res);
    }
}
