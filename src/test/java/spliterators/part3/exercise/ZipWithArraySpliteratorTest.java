package spliterators.part3.exercise;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertThat;

public class ZipWithArraySpliteratorTest {


    private String[] generateStringArrayWithSize(int size) {
        String[] scope = {"Random 1", "Random 2", "Random 3", "Random 4", "Random 5", "Random 6"};
        Random random = new Random();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = scope[random.nextInt(scope.length)];
        }
        return result;
    }

    private List<Pair<String, String>> combaineToPairs(String[] array1, String[] array2) {
        List<Pair<String, String>> list = new ArrayList<>();
        int minLength = Math.min(array1.length, array2.length);
        for (int i = 0; i < minLength; i++) {
            list.add(new Pair<>(array1[i], array2[i]));
        }
        return list;
    }

    @Test
    public void comparePaired() {
        final String[] englishLevelString = {"Elementary",
                "Pre-Intermediate",
                "Intermediate",
                "Pro-Intermediate",
                "Advanced",
                "Pro-Advanced",
                "Expert",
                "Pro-Expert"};
        final String[] englishLevelNum = {"A1", "A2", "B1", "B2", "C1", "C2"/*,"D1", "D2", "E1", "E2"*/};
        Spliterator<String> spliterator = Arrays.spliterator(englishLevelString);
        StreamSupport.stream(new ZipWithArraySpliterator<>(spliterator, englishLevelNum), true)
                .forEach(s -> System.out.println(s.getA() + " " + s.getB()));

    }

    @Test
    public void test1() {
        String[] largeArray = generateStringArrayWithSize(10);
        String[] smallArray = generateStringArrayWithSize(5);
        List<Pair<String, String>> expected = combaineToPairs(largeArray, smallArray);

        Spliterator<String> spliterator1 = Arrays.spliterator(largeArray);
        List<Pair<String, String>> result1 = StreamSupport.stream(new ZipWithArraySpliterator<>(spliterator1, smallArray), true)
                .collect(Collectors.toList());
        assertThat(result1, Is.is(expected));


        Spliterator<String> spliterator2 = Arrays.spliterator(smallArray);
        List<Pair<String, String>> result2 = StreamSupport.stream(new ZipWithArraySpliterator<>(spliterator2, smallArray), true)
                .collect(Collectors.toList());
        assertThat(result2, Is.is(expected));
    }
}