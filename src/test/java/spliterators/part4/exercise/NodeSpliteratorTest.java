package spliterators.part4.exercise;

import org.junit.BeforeClass;
import org.junit.Test;
import spliterators.part4.data.Node;

import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class NodeSpliteratorTest {
    private static Node<String> root;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final double CUTOFF = 0.5;

    private static int numNodes;

    private static Node<String> createNodeRecursively(String current) {
        numNodes++;
        return new Node<>(current,
                random.nextDouble() > CUTOFF ? createNodeRecursively(current + "L") : null,
                random.nextDouble() > CUTOFF ? createNodeRecursively(current + "R") : null);
    }

    private String simpleWalkAroundTree(Node<String> n) {
        if (n == null) return "";

        return simpleWalkAroundTree(n.getLeft()) + "(" + n.getValue() + ")" + simpleWalkAroundTree(n.getRight());
    }

    private String collectStreamWithEndCaps(Stream<String> s) {
        return "(" + s.collect(Collectors.joining(")(")) + ")";
    }

    @BeforeClass
    public static void createTree() {
        numNodes = 0;
        while (numNodes < 10)           // Re-create tree if too small
            root = createNodeRecursively("*");
    }

    @Test
    public void tryAdvanceTest() throws Exception {
        String expectedWalkAround = simpleWalkAroundTree(root);

        String streamWalkaround = collectStreamWithEndCaps(root.stream());

        System.out.println(streamWalkaround);
        assertEquals(expectedWalkAround, streamWalkaround);
    }

    @Test
    public void trySplitTest() throws Exception {
        String expectedWalkAround = simpleWalkAroundTree(root);

        Stream<String> nodeStream = root.stream();
        // Partial traverse before split
        String part1 = collectStreamWithEndCaps(nodeStream.limit(5));

        // Have to reset stream after each terminal operation
        nodeStream = root.stream();
        Spliterator<String> split = nodeStream.skip(5).spliterator().trySplit();
        String part2 = "";
        if (split != null) {
            System.out.println("Split OK");
            part2 = collectStreamWithEndCaps(StreamSupport.stream(split, true));
        } else
            System.out.println("Split failed");     // May fail if left branch of the current root is empty or completely used up

        // Reset stream again
        final Spliterator<String> unSplit = root.stream().skip(5).spliterator();
        unSplit.trySplit();
        String part3 = collectStreamWithEndCaps(StreamSupport.stream(unSplit, true));

        assertEquals(expectedWalkAround, part1 + part2 + part3);

        System.out.printf("First 5: %s\nSplit: %s\nRemainder: %s", part1, part2, part3);
    }
}