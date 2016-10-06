package spliterators.part4.exercise;

import spliterators.part4.data.Node;

import java.util.ArrayDeque;
import java.util.Spliterator;
import java.util.function.Consumer;

public class NodeSpliterator<T> implements Spliterator<Node<T>> {
    ArrayDeque<Node<T>> currentPath;

    private void walkToLeftmost(Node<T> node) {
        Node<T> currentNode = node;
        while (currentNode.getLeft() != null) {
            currentPath.push(currentNode);
            currentNode = currentNode.getLeft();
        }
        currentPath.push(currentNode);
    }

    NodeSpliterator(Node<T> node) {
        currentPath = new ArrayDeque<>();
        walkToLeftmost(node);
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Node<T>> action) {
        if (currentPath.isEmpty()) return false;

        final Node<T> currentNode = currentPath.pop();
        action.accept(currentNode);
        Node<T> right = currentNode.getRight();
        if (right != null)
            walkToLeftmost(right);
        return true;
    }

    @Override
    public Spliterator<Node<T>> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return NONNULL + ORDERED;
    }
}
