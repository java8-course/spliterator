package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Spliterator;
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
    public void checkSpliterators(){
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        int [][] matrix = new int[N][N];
        for (int i = 0; i < N; ++i){
            for (int k = 0; k < N; ++k){
                matrix[i][k] = rand.nextInt(100);
            }
        }
        checkSpliterator(matrix,true);
        checkSpliterator(matrix,false);
    }

    private void checkSpliterator(int [][]matrix,boolean parallel){
        Assert.assertEquals(N*N-15,getStream(matrix,parallel).skip(15).count());
        Assert.assertEquals(6,getStream(matrix,parallel).limit(6).count());
        Assert.assertEquals(3,getStream(matrix,parallel).skip(N*N-3).limit(6).count());

        int expectedSum = 0;
        for (int i = 0; i < N; ++i){
            for (int k = 0; k < N; ++k){
                expectedSum += matrix[i][k];
            }
        }

        Assert.assertEquals(expectedSum,getStream(matrix,parallel).sum());
        Assert.assertEquals(expectedSum-matrix[0][0],getStream(matrix,parallel).skip(1).sum());

        final int negativeSum = getStream(matrix, parallel).map(i -> i * -1).skip(1).sum();
        Assert.assertEquals(expectedSum*-1+matrix[0][0],negativeSum);
        matrix[N-1][N-1] = 2;
        Assert.assertTrue(getStream(matrix, parallel).anyMatch(i -> i < 10));

    }

}
