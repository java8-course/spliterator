package spliterators.part2.example;

import org.junit.Test;
import spliterators.part1.example.ArrayExample;
import spliterators.part3.exercise.ZipWithArraySpliterator;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by whobscr on 18.07.17.
 */
public class ZipWithArraySpliteratorTest {

    private int[] getRandomArray(int length) {
        final int[] result = new int[length];

        for (int i = 0; i < length; i++)
            result[i] = ThreadLocalRandom.current().nextInt();

        return result;
    }

    @Test
    public void zerosSuccess() {
        final int[] randomArray = getRandomArray(1000);
        final int[] array = getRandomArray(1000);
        StreamSupport.stream(new ZipWithArraySpliterator(new ArrayExample.IntArraySpliterator(randomArray), array))
            .collect(Collectors.toList());
    }
}
