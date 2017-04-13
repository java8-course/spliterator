package spliterators.part2.exercise;

import org.junit.Test;
import spliterators.part1.exercise.RectangleSpliterator;
import spliterators.part3.exercise.Pair;
import spliterators.part3.exercise.ZipWithArraySpliterator;

import javax.xml.stream.events.Characters;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class ZipWithArraySpliteratorTest {

    @Test
    public void testByStream() {

        Spliterator<String> inner = Stream.of("Hello, spliterators!".split("")).spliterator();
        Double[] array = new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

        ZipWithArraySpliterator<String, Double> spliterator = new ZipWithArraySpliterator<>(inner, array);

        double res1 = StreamSupport.stream(spliterator, true)
                .mapToDouble(Pair::getB)
                .sum();
        assertThat(res1, is(21.0));

//        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
//        double res2 = StreamSupport.stream(spliterator, true)
//                .skip(3)
//                .mapToDouble(IndexedDoublePair::getValue)
//                .sum();
//        assertThat(res2, is(15.0));
//
//        spliterator = new ZipWithIndexDoubleSpliterator(Arrays.spliterator(data));
//        double res3 = StreamSupport.stream(spliterator, true)
//                .mapToDouble(IndexedDoublePair::getValue)
//                .reduce((e1, e2) -> e1 * e2)
//                .getAsDouble();
//        assertThat(res3, is(720.0));
    }

}
