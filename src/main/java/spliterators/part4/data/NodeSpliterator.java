package spliterators.part4.data;

import java.util.HashSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;

public class NodeSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private Node<T> node;
    private HashSet<Node<T>> visited = new HashSet<>();

    private Stack<Node<T>> next = new Stack<>();

    public NodeSpliterator(Node<T> node) {
        super(node.size(), 0);
        this.node = node;
        this.next.add(node);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (next.isEmpty()) {
            return false;
        }
        Node<T> node = next.pop();
        visited.add(node);

        if (node.getLeft() != null && !visited.contains(node.getLeft())) {
            visited.add(node.getLeft());
            next.push(node.getLeft());
        }
        if (node.getLeft() != null && !visited.contains(node.getRight())) {
            visited.add(node.getRight());
            next.push(node.getRight());
        }

        action.accept(node.getValue());
        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        if (next.empty()) {
            return null;
        }

        if (next.size() >= 2) {
            return new NodeSpliterator<>(next.pop());
        }

        if (node.getLeft() == null || visited.contains(node.getLeft())
                || node.getRight() == null || visited.contains(node.getRight())) {
            return null;
        }

        visited.add(node.getLeft());

        return new NodeSpliterator<>(node.getLeft());
    }
}
