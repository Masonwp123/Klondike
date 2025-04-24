import java.util.function.Consumer;
import java.util.function.Predicate;

public class Stack<T> {

    protected int length = 0;
    protected Node<T> head = null;

    // Returns true if payload can be pushed
    public boolean canPush(T nextPayload) {
        return true;
    }

    // Pushes payload to the top of the stack
    public void push(T nextPayload) {
        if (canPush(nextPayload)) {
            head = new Node<>(nextPayload, head);
            length++;
        }
    }

    // Pushes payload to the top of the stack
    public void pushNode(Node<T> nextNode) {
        if (canPush(nextNode.get())) {
            nextNode.setNext(head);
            head = nextNode;
            length++;
        }
    }

    public int getLength() {
        return length;
    }

    // Gets item from the top of the stack but does not remove
    public T top() {
        return topNode().get();
    }

    // Gets Node from the top of the stack but does not remove
    public Node<T> topNode() {
        return head;
    }

    // Removes item from the top of the stack
    public T pop() {
        return popNode().get();
    }

    // Removes Node from the top of the stack
    public Node<T> popNode() {
        length--;
        Node<T> topNode = head;
        head = topNode.getNext();
        return topNode;
    }

    // Pulls if the predicate returns true O(n)
    public T pullFirst(Predicate<T> predicate) {

        // Ensure head doesn't pass predicate so previous is not null
        if (predicate.test(top())) {
            return pop();
        }

        Node<T> previous = topNode();
        Node<T> current = topNode().getNext();
        while (current != null) {
            if (predicate.test(current.get())) {
                previous.setNext(current.getNext());
                length--;
                return current.get();
            }
            previous = current;
            current = current.getNext();
        }

        return null;
    }

    // Pulls from a specified index O(index)
    public T pull(int index) {
        if (index < 0 || index > length - 1) {
            return null;
        }
        if (index == 0) {
            return pop();
        }

        Node<T> previous = topNode();
        Node<T> current = topNode().getNext();

        // Subtract one because we already are on second element
        for (int i = 0; i < index - 1; ++i) {
            previous = current;
            current = current.getNext();
        }

        previous.setNext(current.getNext());
        length--;

        return current.get();
    }

    // Pulls the object if equal O(n)
    // Ensures the object is not in the stack
    public T pull(T payload) {
        pullFirst(o -> {
            return o.equals(payload);
        });
        return payload;
    }

    // Pulls the object if equal O(n)
    // Ensures the object is not in the stack
    public T pullNode(Node<T> object) {
        pullFirst(o -> {
            return o.equals(object.get());
        });
        return object.get();
    }

    // Pops head to other stack
    public void popTo(Stack<T> other) {
        T payload = this.top();
        if (!other.canPush(payload)) {
            return;
        }
        other.push(this.pull(payload));
    }

    // Swaps payload to other stack
    public void swapTo(Stack<T> other, T payload) {
        if (!this.contains(payload)) {
            return;
        }
        if (!other.canPush(payload)) {
            return;
        }
        other.push(this.pull(payload));
    }

    // Gets if the predicate returns true
    public T getFirst(Predicate<T> predicate) {

        Node<T> current = topNode();
        while (current != null) {
            if (predicate.test(current.get())) {
                return current.get();
            }
            current = current.getNext();
        }

        return null;
    }

    // Iterate through stack and clear all nexts in an attempt to have them deleted faster
    public void empty() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setNext(null);
            current = next;
        }
        head = null;
        length = 0;
    }

    // Returns true if the stack has a next
    public boolean hasNext() {
        return !isEmpty() && head.getNext() != null;
    }

    // Returns true if Stack is empty
    public boolean isEmpty() {
        return head == null;
    }

    public boolean contains(T payload) {
        Node<T> current = head;
        while (current != null) {
            if (current.get().equals(payload)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // Run through stack and have a callback for each node
    // Runs from top of stack to bottom
    public void iterate(Consumer<T> callback) {
        Node<T> current = head;
        while (current != null) {
            callback.accept(current.get());
            current = current.getNext();
        }
    }

    // Run through stack and have a callback for each node
    // Runs from top of stack to bottom
    public void iterateUntil(Predicate<T> callback) {
        Node<T> current = head;
        while (current != null) {
            if (!callback.test(current.get())) {
                break;
            }
            current = current.getNext();
        }
    }
}
