package spliterators.part4.data;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class NodeSpliterator<T> extends Spliterators.AbstractSpliterator<Node<T>> {

    private final Node<T> node;
    private boolean rightDone, leftDone;

    public NodeSpliterator(final Node<T> node) {
        super(Integer.MAX_VALUE, IMMUTABLE);
        this.node = node;
    }

    @Override
    public void forEachRemaining(final Consumer<? super Node<T>> action) {
        if (node != null) {
            forEach(node, action);
        }
    }

    private void forEach(final Node<T> node, final Consumer<? super Node<T>> action) {
        action.accept(node);
        if (node.getLeft() != null) {
            forEach(node.getLeft(), action);
        }
        if (node.getRight() != null) {
            forEach(node.getRight(), action);
        }
    }

    @Override
    public Spliterator<Node<T>> trySplit() {
        if ((node == null)
                ||
                (rightDone && leftDone)) {
            return null;
        }
        if (!leftDone) {
            leftDone = true;
            return new NodeSpliterator<>(node.getLeft());
        } else {
            rightDone = true;
            return new NodeSpliterator<>(node.getRight());
        }
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Node<T>> action) {
        if (!leftDone) {
            action.accept(node.getLeft());
            leftDone = true;
        } else if (!rightDone) {
            action.accept(node.getRight());
            rightDone = true;
        } else {
            action.accept(node);
            return false;
        }
        return true;
    }

    @Override
    public long estimateSize() {
        return Integer.MAX_VALUE;
    }
}
