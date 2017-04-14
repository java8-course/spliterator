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

        double res1 = StreamSupport.stream(createSpliterator(), true)
                .mapToDouble(Pair::getB)
                .sum();
        assertThat(res1, is(21.0));

        double res2 = StreamSupport.stream(createSpliterator(), true)
                .skip(3)
                .mapToDouble(Pair::getB)
                .limit(2)
                .sum();
        assertThat(res2, is(9.0));

        double res3 = StreamSupport.stream(createSpliterator(), true)
                .mapToDouble(Pair::getB)
                .reduce((e1, e2) -> e1 * e2)
                .getAsDouble();
        assertThat(res3, is(720.0));
    }

    private ZipWithArraySpliterator<String, Double> createSpliterator() {
        Spliterator<String> inner = Stream.of("Hello, spliterators!".split("")).spliterator();
        Double[] array = new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        return new ZipWithArraySpliterator<>(inner, array);
    }

}
