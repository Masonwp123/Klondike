
public class Node<T> {
    protected T payload;
    protected Node<T> next;

    public Node(T payload, Node<T> next) {
        this.payload = payload;
        this.next = next;
    }

    public Node(T payload) {
        this(payload, null);
    }

    public Node() {
        this(null, null);
    }

    public T get() {
        return this.payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}