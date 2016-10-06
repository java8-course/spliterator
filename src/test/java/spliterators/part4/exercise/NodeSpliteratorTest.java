package spliterators.part4.exercise;

import org.junit.Before;
import org.junit.Test;
import spliterators.part4.data.Node;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class NodeSpliteratorTest {
    private Node<String> root;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final double CUTOFF = 0.5;

    private int numNodes;

    private Node<String> createNodeRecursively(String current) {
        numNodes++;
        return new Node<>(current,
                random.nextDouble() > CUTOFF ? createNodeRecursively(current + "L") : null,
                random.nextDouble() > CUTOFF ? createNodeRecursively(current + "R") : null);
    }

    private String walkAroundTree(Node<String> n) {
        if (n == null) return "";

        return walkAroundTree(n.getLeft()) + "(" + n.getValue() + ")" + walkAroundTree(n.getRight());
    }

    @Before
    public void createTree() {
        numNodes = 0;
        while (numNodes < 10)           // Re-create tree if too small
            root = createNodeRecursively("*");
    }


    @Test
    public void tryAdvance() throws Exception {
        String expectedWalkAround = walkAroundTree(root);

        String streamWalkaround = "(" +
                root.stream().collect(Collectors.joining(")("))
                + ")";

        System.out.println(streamWalkaround);
        assertEquals(expectedWalkAround, streamWalkaround);
    }

}