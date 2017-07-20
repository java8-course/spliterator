package spliterators.part1.exercise;

import org.junit.Test;

import java.util.Spliterator;
import java.util.function.IntConsumer;

import static org.junit.Assert.*;


public class RectangleSpliteratorTest {
    @Test
    public void trySplit() throws Exception {

        final int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16},
        };

        RectangleSpliterator sp = new RectangleSpliterator(arr);

        Spliterator.OfInt ofInt1 = sp.trySplit();
        Spliterator.OfInt ofInt2 = ofInt1.trySplit();
        Spliterator.OfInt ofInt3 = ofInt2.trySplit();

        boolean b1 = ofInt3.tryAdvance((IntConsumer) value -> assertEquals(15, value));
        boolean b2 = ofInt3.tryAdvance((IntConsumer) value -> assertEquals(16, value));
        boolean b3 = ofInt3.tryAdvance((IntConsumer) value -> System.out.println("This message you'll never see!"));
        assertTrue(b1);
        assertTrue(b2);
        assertFalse(b3);

        boolean b4 = ofInt2.tryAdvance((IntConsumer) value -> assertEquals(13, value));
        boolean b5 = ofInt2.tryAdvance((IntConsumer) value -> assertEquals(14, value));
        boolean b6 = ofInt2.tryAdvance((IntConsumer) value -> System.out.println("This message you'll never see!"));
        assertTrue(b4);
        assertTrue(b5);
        assertFalse(b6);
    }


    @Test
    public void trySplit2() throws Exception {
        final int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16},
                {17, 18, 19, 20}
        };

        RectangleSpliterator sp = new RectangleSpliterator(arr);

        Spliterator.OfInt ofInt1 = sp.trySplit();
        Spliterator.OfInt ofInt2 = ofInt1.trySplit();
        Spliterator.OfInt ofInt3 = ofInt1.trySplit();

        boolean b1 = ofInt1.tryAdvance((IntConsumer) value -> assertEquals(11, value));
        boolean b2 = ofInt1.tryAdvance((IntConsumer) value -> assertEquals(12, value));
        boolean b3 = ofInt1.tryAdvance((IntConsumer) value -> System.out.println("This message you'll never see!"));
        assertTrue(b1);
        assertTrue(b2);
        assertFalse(b3);

        boolean b4 = ofInt3.tryAdvance((IntConsumer) value -> assertEquals(13, value));
        boolean b5 = ofInt3.tryAdvance((IntConsumer) value -> assertEquals(14, value));
        boolean b6 = ofInt3.tryAdvance((IntConsumer) value -> assertEquals(15, value));
        boolean b7 = ofInt3.tryAdvance((IntConsumer) value -> System.out.println("This message you'll never see!"));
        assertTrue(b4);
        assertTrue(b5);
        assertTrue(b6);
        assertFalse(b7);
    }

    @Test
    public void forEachRemaining() {
        final int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16},
                {17, 18, 19, 20}
        };

        RectangleSpliterator sp = new RectangleSpliterator(arr);

        Spliterator.OfInt ofInt1 = sp.trySplit();
        Spliterator.OfInt ofInt2 = ofInt1.trySplit();
        Spliterator.OfInt ofInt3 = ofInt1.trySplit();

        System.out.println("ofInt1");
        ofInt1.forEachRemaining((IntConsumer) System.out::println);
        System.out.println("ofInt2");
        ofInt2.forEachRemaining((IntConsumer) System.out::println);
        System.out.println("ofInt3");
        ofInt3.forEachRemaining((IntConsumer) System.out::println);
    }

}