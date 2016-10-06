package spliterators.part4.exercise;

import spliterators.part4.data.Node;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Spliterator for nodes of type T.
 * Walking through leftmost branch is eager (on creation, advance and each split),
 * walking through other branches is lazy.
 * @param <T> Node type
 */
public class NodeSpliterator<T> implements Spliterator<T> {
    private ArrayDeque<Node<T>> currentPath;

    private void walkToLeftmost(Node<T> node) {
        Node<T> currentNode = node;
        while (currentNode.getLeft() != null) {
            currentPath.push(currentNode);
            currentNode = currentNode.getLeft();
        }
        currentPath.push(currentNode);
    }

    private NodeSpliterator(ArrayDeque<Node<T>> startingPath) {
        currentPath = startingPath;
    }

    public NodeSpliterator(Node<T> root) {
        Objects.requireNonNull(root);
        currentPath = new ArrayDeque<>();
        walkToLeftmost(root);
    }

    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
        if (currentPath.isEmpty()) return false;

        final Node<T> currentNode = currentPath.pop();
        action.accept(currentNode.getValue());
        Node<T> right = currentNode.getRight();
        if (right != null)
            walkToLeftmost(right);
        return true;
    }

    /**
     * Everything left of the current root node gets split off.
     * Root node remains with the current spliterator.
     */
    @Override
    public Spliterator<T> trySplit() {
        if (currentPath.size() < 2) return null;

        final Node<T> root = currentPath.removeLast();
        final NodeSpliterator<T> newSplit = new NodeSpliterator<>(currentPath);
        currentPath = new ArrayDeque<>();
        currentPath.push(root);
        return newSplit;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;          // This spliterator is NOT SIZED
    }

    @Override
    public int characteristics() {
        return NONNULL + ORDERED + IMMUTABLE;
    }
}
