package spliterators.part1.example;


import org.junit.Test;
import spliterators.part1.exercise.RectangleSpliterator;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RectangleSpliteratorTest {

    private String[] getRandomArray(int length) {
        final String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            result[i] = String.valueOf(ThreadLocalRandom.current().nextInt());
        }

        return result;
    }

    @Test
    public void tryAdvanceTest() {
        int[][] data = new int[][] {{1, 2}, {3, 4}, {5, 6}};
        RectangleSpliterator spliterator = new RectangleSpliterator(data);

        spliterator.tryAdvance((Integer i) -> assertThat(i, is(1)));
        spliterator.tryAdvance((Integer i) -> assertThat(i, is(2)));
        spliterator.tryAdvance((Integer i) -> assertThat(i, is(3)));
        spliterator.tryAdvance((Integer i) -> assertThat(i, is(4)));
        spliterator.tryAdvance((Integer i) -> assertThat(i, is(5)));
        spliterator.tryAdvance((Integer i) -> assertThat(i, is(6)));
        assertFalse(spliterator.tryAdvance((Integer i) -> {}));
    }

    @Test
    public void trySplitTest() {
        int[][] data = new int[][] {{1, 2}, {3, 4}, {5, 6}};
        RectangleSpliterator spliterator = new RectangleSpliterator(data);

        int res1 = StreamSupport.intStream(spliterator, true)
                .sum();
        assertThat(res1, is(21));

        int res2 = StreamSupport.intStream(new RectangleSpliterator(data), true)
                .skip(3)
                .sum();
        assertThat(res2, is(15));

        int res3 = StreamSupport.intStream(new RectangleSpliterator(data), true)
                .reduce((e1, e2) -> e1*e2)
                .getAsInt();
        assertThat(res3, is(720));

        boolean res4 = StreamSupport.intStream(new RectangleSpliterator(data), true)
                .filter(i ->  i%2==0)
                .anyMatch(i -> i<2);
        assertFalse(res4);

    }
}
