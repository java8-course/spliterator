package spliterators.part4.data;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;

public class NodeSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private Node<T> node;
    private final Stack<Node<T>> toDo;

    public NodeSpliterator(final Node<T> node) {
        super(Integer.MAX_VALUE, IMMUTABLE);
        this.node = node;
        toDo = new Stack<>();
    }

    @Override
    public void forEachRemaining(final Consumer<? super T> action) {
        if (node != null) {
            forEach(node, toDo);
        }
        toDo.forEach(n -> {
            action.accept(n.getValue());
        });
    }

    private void forEach(final Node<T> node, final Stack<Node<T>> stack) {
        stack.add(node);
        if (node.getLeft() != null) {
            forEach(node.getLeft(), stack);
        }
        if (node.getRight() != null) {
            forEach(node.getRight(), stack);
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        if (node == null) {
            return null;
        }
        final NodeSpliterator<T> left = new NodeSpliterator<>(node.getLeft());
        toDo.add(node);
        node = node.getRight();
        return left;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
        if (toDo.isEmpty()) {
            return false;
        }
        final Node<T> pop = toDo.pop();
        action.accept(pop.getValue());
        return true;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }
}
