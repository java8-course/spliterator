package spliterators.part4.exercise;

import org.junit.Before;
import org.junit.Test;
import spliterators.part4.data.Node;

import java.util.concurrent.ThreadLocalRandom;

public class NodeSpliteratorTest {
    private Node<String> root;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final double CUTOFF = 0.5;

    private Node<String> createNodeRecursively(String current) {
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
        root = createNodeRecursively("*");
    }


    @Test
    public void tryAdvance() throws Exception {
        String expectedWalkAround = walkAroundTree(root);
        System.out.println(expectedWalkAround);
    }

}