package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class RectangleSpliteratorTest {

    public static final int N = 5;

    private static IntStream getStream(int[][] matrix, boolean parallel) {
        return StreamSupport.intStream(new RectangleSpliterator(matrix), parallel);
    }

    @Test
    public void checkSpliterator(){
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        int [][] matrix = new int[N][N];
        long expectedSum = 0;
        for (int i = 0; i < N; ++i){
            for (int k = 0; k < N; ++k){
                matrix[i][k] = rand.nextInt(100);
                expectedSum += matrix[i][k];
            }
        }

        Assert.assertEquals(N*N-5,getStream(matrix,false).skip(5).count());
        Assert.assertEquals(N*N-15,getStream(matrix,true).skip(15).count());

        Assert.assertEquals(expectedSum,getStream(matrix,true).sum());
        Assert.assertEquals(expectedSum,getStream(matrix,false).sum());

        Assert.assertEquals(expectedSum-matrix[0][0],getStream(matrix,true).skip(1).sum());
        Assert.assertEquals(expectedSum-matrix[0][0],getStream(matrix,false).skip(1).sum());

        final int parNegativeSum = getStream(matrix, true).map(i -> i * -1).skip(1).sum();
        Assert.assertEquals(expectedSum*-1+matrix[0][0],parNegativeSum);
        final int seqNegativeSum = getStream(matrix, false).map(i -> i * -1).skip(1).sum();
        Assert.assertEquals(expectedSum*-1+matrix[0][0],seqNegativeSum);

        matrix[N-1][N-1] = 2;
        Assert.assertTrue(getStream(matrix, false).anyMatch(i -> i < 10));
        Assert.assertTrue(getStream(matrix, true).anyMatch(i -> i < 10));

    }

}
