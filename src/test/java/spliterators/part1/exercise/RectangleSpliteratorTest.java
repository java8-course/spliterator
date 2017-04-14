package spliterators.part1.exercise;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RectangleSpliteratorTest {

    private int[][] getRandomArray(int count, int length) {
        final int[][] result = new int[count][length];

        for (int i = 0; i < count; i++)
            for (int j = 0; j < length; j++)
                result[i][j] = j;

        return result;
    }

    @Test
    public void parallelAndSeqGiveSameResults() {
        final int[][] randomArray = getRandomArray(4, 4);

        long count = StreamSupport.stream(new RectangleSpliterator(randomArray), true)
                .count();
        long count2 = StreamSupport.stream(new RectangleSpliterator(randomArray), true)
                .skip(7)
                .count();
        int n = 7;
        List<Integer> result2 = StreamSupport.stream(new RectangleSpliterator(randomArray), true)
                .skip(n)
                .collect(Collectors.toList());

        assertThat(count, is(16L));
        assertThat(count2, is(9L));
        assertThat(result2.get(0), is(randomArray[0][n % randomArray[0].length]));
    }
}