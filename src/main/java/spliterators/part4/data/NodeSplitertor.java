package spliterators.part4.data;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by hamster on 15.01.17.
 */
public class NodeSplitertor <T> implements Spliterator <T> {

    private LinkedList <Node<T>> branch;

    private LinkedList <Node<T>> getBranch(Node<T> node) {
        LinkedList <Node<T>> result = new LinkedList<>();
        Node<T> currentNode = node;
        while (currentNode.getLeft() != null) {
            result.add(currentNode.getLeft());
            currentNode = currentNode.getLeft();
        }
        result.add(currentNode);
        return result;
    }

    public NodeSplitertor (Node<T> node) {
        branch = getBranch(node);
    }

    private NodeSplitertor(LinkedList<Node<T>> startBranch) {
        branch = startBranch;
    }


    @Override
    public boolean tryAdvance(Consumer <? super T> action) {
        if (branch.isEmpty()) {
            return false;
        }
        Node<T> current = branch.removeLast();
        action.accept(current.getValue());
        current = current.getRight();
        if (current != null) {
            branch = getBranch(current);
        }
        return true;
    }

    @Override
    public Spliterator <T> trySplit() {
        if (branch.size() >= 2) {
            final Node <T> top = branch.removeFirst();
            final NodeSplitertor <T> result = new NodeSplitertor <T> (branch);
            branch.clear();
            branch.add(top);
            return result;
        }
        else if (branch.size() == 1) {
            final Node <T> top = branch.getLast();
            final Node <T> right = top.getRight();
            if (right != null) {
                branch = getBranch(right);
                return new tSpliterator<T>(top.getValue());
            } else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL ;
    }

    private class tSpliterator<T> implements Spliterator <T> {

        private final T current;
        private boolean last = false;

        private tSpliterator (T current) {
            this.current = current;
        }

        @Override
        public boolean tryAdvance(Consumer <? super T> action) {
            if (last) {
                return false;
            }
            action.accept(current);
            last = true;
            return true;
        }

        @Override
        public Spliterator <T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            if (last) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int characteristics() {
            return Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL |
                    Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }
}
