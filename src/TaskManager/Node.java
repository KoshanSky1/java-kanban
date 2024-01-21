package TaskManager;
import Models.Task;

public class Node {

    private Task value;
    private Node previous;
    private Node next;

    Node(Task value, Node previous, Node next) {

        this.value = value;

        this.previous = previous;

        this.next = next;

    }

    public Task getValue() {

        return value;

    }

    public Node getPrevious() {

        return previous;

    }

    public void setPrevious(Node previous) {

        this.previous = previous;

    }

    public Node getNext() {

        return next;

    }

    public void setNext(Node next) {

        this.next = next;

    }

}