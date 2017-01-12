package spliterators.part1.exercise;

import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

public class RectangleSpliteratorTest {

    public static final int N = 5;

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
        final long actualSeqSum = StreamSupport.intStream(new RectangleSpliterator(matrix), false).sum();
        Assert.assertEquals(expectedSum,actualSeqSum);

        final RectangleSpliterator spliterator = new RectangleSpliterator(matrix);
        Assert.assertEquals(N*N,spliterator.estimateSize());

        final long actualParSum = StreamSupport.intStream(spliterator, true).sum();
        Assert.assertEquals(expectedSum,actualParSum);
    }
}
