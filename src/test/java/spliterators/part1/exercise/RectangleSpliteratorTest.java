package spliterators.part1.exercise;

import org.junit.Test;

import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RectangleSpliteratorTest {
    @Test
    public void test() {
        int[][] array = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };

//        int[][] array = {
//                {1, 2, 3, 4, 5, 6, 7}
//                };

        final boolean parallel = true;
        long sum = StreamSupport.intStream(new RectangleSpliterator(array), parallel)
                .asLongStream()
                .sum();
        System.out.println("Sum: " + sum);

        StreamSupport.intStream(new RectangleSpliterator(array), parallel).forEach(value -> System.out.print(value + " "));
        assertEquals(sum, 78);
    }

}