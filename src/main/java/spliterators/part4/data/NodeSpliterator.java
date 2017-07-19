package spliterators.part4.data;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;

public class NodeSpliterator<T> extends Spliterators.AbstractSpliterator<Node<T>> {

    private Node<T> node;
    private Stack<Node<T>> toDo;

    public NodeSpliterator(Node<T> node) {
        super(Integer.MAX_VALUE, IMMUTABLE
                | CONCURRENT);
        this.node = node;
        toDo = new Stack<>();
    }

    @Override
    public void forEachRemaining(final Consumer<? super Node<T>> action) {
        forEach(node, toDo);
        toDo.forEach(n -> {
            if (n != null) {
                action.accept(n);
            }
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
    public Spliterator<Node<T>> trySplit() {
        if ((node.getLeft() != null)
                && (node.getRight() != null)) {
            final NodeSpliterator<T> left = new NodeSpliterator<>(node.getLeft());
            toDo.add(node);
            node = node.getRight();
            return left;
        }
        return null;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Node<T>> action) {
        if (!toDo.isEmpty()) {
            final Node<T> pop = toDo.pop();
            if (pop != null) {
                action.accept(pop);
            }
        }
        return toDo.isEmpty();
    }

    @Override
    public long estimateSize() {
        return Integer.MAX_VALUE;
    }
}
