package spliterators.part4.data;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Node<T> {
    private final T value;
    private final Node<T> left;
    private final Node<T> right;
    private int size = 1;

    public Node(T value) {
        this(value, null, null);
    }

    public Node(T value, Node<T> left, Node<T> right) {
        this.value = Objects.requireNonNull(value);
        this.left = left;
        this.right = right;

        if (this.left != null) {
            size += this.left.size();
        }

        if (this.right != null) {
            size += this.right.size();
        }
    }

    public T getValue() {
        return value;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }

    public int size() {
        return size;
    }

    public Stream<T> stream(boolean parallel) {
        return StreamSupport.stream(new NodeSpliterator(this), parallel);
    }
}
