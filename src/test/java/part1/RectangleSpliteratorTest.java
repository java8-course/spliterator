package part1;

import org.junit.Assert;
import org.junit.Test;
import spliterators.part1.exercise.RectangleSpliterator;

import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * Created by nikita on 4/11/2017.
 */
public class RectangleSpliteratorTest {

    private int[][] getRandomArray(final int outerLength, final int innerLenght) {
        final int[][] result = new int[outerLength][innerLenght];

        for (int i = 0; i < outerLength; i++) {
            for(int j = 0; j< innerLenght; j++){
                result[i][j] = j;
            }
        }

        return result;
    }

    @Test
    public void trySplit(){
        final int[][] array = getRandomArray(2,4);
        final long actual = StreamSupport.stream(new RectangleSpliterator(array), true).count();
        final long expected = Arrays.stream(array).parallel().flatMapToInt(Arrays::stream).count();
        final long actual2 = StreamSupport.stream(new RectangleSpliterator(array), true).skip(5).count();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, actual2);
    }

}
