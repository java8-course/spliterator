package spliterators.part4.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Created by hamster on 15.01.17.
 */
public class NodeSplitertorTest {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private Node <Integer> getNode() {
        return new Node(random.nextInt(), Math.random() < 0.5 ? getNode() : null, Math.random() < 0.5 ? getNode() : null);
    }

    @Test
    public void anyMatchTest() {
        Node <Integer> node = getNode();
        Node <Integer> findActual = node;
        List <Integer> leftValues = new ArrayList();
        List <Integer> rightValues = new ArrayList();

        while (findActual.getLeft() != null) {
            leftValues.add(findActual.getValue());
            findActual = node.getLeft();
        }
        for (Integer i: leftValues) {
            Assert.assertTrue(StreamSupport.stream(new NodeSplitertor<>(node), true)
                    .anyMatch(value -> value.equals(i)));
        }
        findActual = node;
        while (findActual.getRight() != null) {
            rightValues.add(findActual.getValue());
            findActual = node.getRight();
        }
        for (Integer i: rightValues) {
            Assert.assertTrue(StreamSupport.stream(new NodeSplitertor<>(node), true)
                    .anyMatch(value -> value.equals(i)));
        }
    }

}