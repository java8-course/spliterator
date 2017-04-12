package spliterators.part2.exercise;

import org.junit.Test;
import spliterators.part1.exercise.RectangleSpliterator;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class ZipWithIndexDoubleSpliteratorTest {

    @Test
    public void testByStream() {
        double[] data = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        ZipWithIndexDoubleSpliterator spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));

        double res1 = StreamSupport.stream(spliterator, true)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();
        assertThat(res1, is(21.0));

        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
        double res2 = StreamSupport.stream(spliterator, true)
                .skip(3)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();
        assertThat(res2, is(15));

//        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
//        int res3 = StreamSupport.doubleStream(new RectangleSpliterator(data), true)
//                .reduce((e1, e2) -> e1*e2)
//                .getAsInt();
//        assertThat(res3, is(720));
//
//        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
//        boolean res4 = StreamSupport.doubleStream(new RectangleSpliterator(data), true)
//                .filter(i ->  i%2==0)
//                .anyMatch(i -> i<2);
//        assertFalse(res4);
    }

}