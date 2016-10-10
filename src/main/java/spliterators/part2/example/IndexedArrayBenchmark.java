package spliterators.part2.example;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class IndexedArrayBenchmark {

    @Param({"100000"})
    public int length;

    public Integer[] array;

    @Setup
    public void setup() {
        array = new Integer[length];
        for (int i = 0; i < length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
        }
    }

    @Benchmark
    public long baseline_seq() {
        return Arrays.stream(array)
                .sequential()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Benchmark
    public long baeline_par() {
        return Arrays.stream(array)
                .parallel()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Benchmark
    public long rectangle_seq() {
        final boolean parallel = false;
        return StreamSupport.stream(new ArrayZipWithIndexExample.IndexedArraySpliterator<>(array), parallel)
                .map(IndexedPair::getValue)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Benchmark
    public long rectangle_par() {
        final boolean parallel = true;
        return StreamSupport.stream(new ArrayZipWithIndexExample.IndexedArraySpliterator<>(array), parallel)
                .map(IndexedPair::getValue)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
