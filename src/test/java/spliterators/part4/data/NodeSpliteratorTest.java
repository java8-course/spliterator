package spliterators.part4.data;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NodeSpliteratorTest {

    private Node<Integer> getTestData() {
        Node<Integer> n1 = new Node<>(1);
        Node<Integer> n2 = new Node<>(2);
        Node<Integer> n3 = new Node<>(3);
        Node<Integer> n4 = new Node<>(4);
        Node<Integer> n5 = new Node<>(5, n1, n2);
        Node<Integer> n6 = new Node<Integer>(6, n3, n4);
        Node<Integer> n7 = new Node<Integer>(7, n5, n6);
        return n7;
    }


    @Test
    public void testStream() {
        Node<Integer> node = getTestData();
        long value = node.stream(false)
                .mapToInt(val -> val)
                .sum();
        assertEquals(1 + 2 + 3 + 4 + 5 + 6 + 7, value);
    }

    @Test
    public void testStreamParallel() {
        Node<Integer> node = getTestData();
        long value = node.stream(true)
                .mapToInt(val -> val)
                .sum();
        assertEquals(1 + 2 + 3 + 4 + 5 + 6 + 7, value);
    }

    @Test
    public void testTryAdvance() {
        Node<Integer> node = new Node<>(3, new Node<>(1), new Node<>(2));
        NodeSpliterator<Integer> nodeSpliterator = new NodeSpliterator<>(node);

        int[] expectedVals = {3, 2, 1};
        for (int expectedVal : expectedVals) {
            nodeSpliterator.tryAdvance(val -> {
                assertEquals((long) expectedVal, (long) val);
            });
        }
        nodeSpliterator.tryAdvance(Assert::assertNull);
    }
}