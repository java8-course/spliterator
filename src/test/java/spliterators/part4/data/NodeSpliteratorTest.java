package spliterators.part4.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class NodeSpliteratorTest {
    private  Node<Integer> head;
    private final static int LIMIT = 10000;
    private Set<Integer> expected;

    private Node<Integer> build(int left, int right) {
        if (left > right) {
            return null;
        }
        final int mid = (left + right) / 2;
        final Node<Integer> l = build(left, mid - 1);
        final Node<Integer> r = build(mid + 1, right);

        return new Node<>(mid, l, r);
    }

    @Before
    public void setUp() {
        head = build(0, LIMIT);
       expected = Stream.iterate(0, i -> i + 1).limit(LIMIT + 1).collect(Collectors.toSet());
    }

    @Test
    public void testSeq() {
        final Set<Integer> collect = head.stream(false).map(Node::getValue).collect(Collectors.toSet());

        assertEquals(collect, expected);

    }

    @Test
    public void testPar() {
        final Set<Integer> collect = head.stream(true).map(Node::getValue).collect(Collectors.toSet());

        assertEquals(collect, expected);

    }
}