package spliterators.part4.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class NodeSplitertorTest {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private Node <Integer> getNode() {
        return new Node <Integer>(random.nextInt(), Math.random() < 0.5 ? getNode() : null, Math.random() < 0.5 ? getNode() : null);
    }

    @Test
    public void anyMatchTest() {
        Node <Integer> node = getNode();
        Node <Integer> findActual = node;
        List <Integer> leftValues = new ArrayList<>();
        List <Integer> rightValues = new ArrayList<>();

        while ((findActual.getLeft() != null) & (leftValues.size() < 100)) {
            leftValues.add(findActual.getValue());
            findActual = node.getLeft();
        }
        System.out.println(leftValues.size());
        for (Integer i: leftValues) {
            Assert.assertTrue(node.stream().anyMatch(value -> value.equals(i)));
        }
        findActual = node;
        while ((findActual.getRight() != null) & (rightValues.size() < 100)) {
            rightValues.add(findActual.getValue());
            findActual = node.getRight();
        }
        System.out.println(rightValues.size());
        for (Integer i: rightValues) {
            Assert.assertTrue(node.stream().anyMatch(value -> value.equals(i)));
        }
    }

}