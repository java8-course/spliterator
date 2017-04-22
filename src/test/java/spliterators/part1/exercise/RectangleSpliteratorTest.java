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

        int[][] array2 = {
                {1, 2, 3, 4, 5, 6, 7}
                };


        assertEquals(TestArray(array, false), 78);
        assertEquals(TestArray(array, true), 78);

        assertEquals(TestArray(array2, false), 28);
        assertEquals(TestArray(array2, true), 28);
    }

    public long TestArray(int[][] arrayToTest, boolean parallel) {
        long sum = StreamSupport.intStream(new RectangleSpliterator(arrayToTest), parallel)
                .asLongStream()
                .sum();

        System.out.print("Sum: " + sum + ". Parallel: " + parallel + ". Mas: ");
        StreamSupport.intStream(new RectangleSpliterator(arrayToTest), parallel).forEach(value -> System.out.print(value + " "));
        System.out.println();
        return sum;
    }

}