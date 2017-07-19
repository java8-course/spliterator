package spliterators.part2.example;

import org.junit.Test;
import spliterators.part1.example.ArrayExample;
import spliterators.part3.exercise.Pair;
import spliterators.part3.exercise.ZipWithArraySpliterator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ZipWithArraySpliteratorTest {

    private Integer[] getRandomArray(int length) {
        final Integer[] result = new Integer[length];

        for (int i = 0; i < length; i++)
            result[i] = ThreadLocalRandom.current().nextInt();

        return result;
    }

    @Test
    public void zerosSuccess() {
        final Integer[] randomArray = getRandomArray(10);
        final Integer[] array = getRandomArray(10);
        final ZipWithArraySpliterator<Integer, Integer> spliterator = new ZipWithArraySpliterator<>(
                Spliterators.spliterator(randomArray, 0), array);
        final List<Pair<Integer, Integer>> pairs = new ArrayList<>();
//        getPairs(spliterator, pairs);

        List<Pair<Integer, Integer>> actual = StreamSupport.stream(spliterator, true)
//                .map(p -> new Pair<>(p.getA().hashCode() + 1, p.getB() + 1))
//                .map(Pair::toString)
                .collect(Collectors.toList());
        System.out.println(pairs);
    }

    private void getPairs(Spliterator<Pair<Integer, Integer>> spliterator, List<Pair<Integer, Integer>> list) {
        final Spliterator<Pair<Integer, Integer>> newSpliterator = spliterator.trySplit();
        if (newSpliterator != null) {
            getPairs(newSpliterator, list);
            getPairs(spliterator, list);
        } else {
            AtomicInteger first = new AtomicInteger();
            AtomicInteger second = new AtomicInteger();
            spliterator.tryAdvance(p -> {
                first.set(p.getA());
                second.set(p.getB());
            });
            list.add(new Pair<>(first.get(), second.get()));
        }
    }
}
