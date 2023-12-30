package TaskManager;

import Models.Task;


import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    public HashMap<Integer, Node> taskNodeMap = new HashMap<>();

    Node head = null;
    Node tail = null;


    @Override
    public void add(Task task) {
        if (taskNodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node addedNode = this.linkLast(task);
        taskNodeMap.put(task.getId(), addedNode);
    }

    @Override
    public void remove(int taskId) {
        Node deletingNode = taskNodeMap.get(taskId);
        removeNode(deletingNode);
        taskNodeMap.remove(taskId);
    }

    @Override
    public ArrayList<Task> getHistory() {   // история просмотров
        return getTasks();
    }

    private Node linkLast(Task task) {
        Node currentNode;

        if (head == null) {
            currentNode = new Node(task, null, null);
            head = currentNode;
            tail = currentNode;
            return currentNode;
        }
        Node currentHead = tail;
        Node newNode = new Node(task, currentHead, null);
        currentHead.setNext(newNode);
        tail = newNode;
        return newNode;
    }

    private void removeNode(Node node) {
        if (node == null) return;
        Node previousNode = node.getPrevious();
        Node nextNode = node.getNext();

        if (previousNode != null && nextNode != null) {
            previousNode.setNext(nextNode);
            nextNode.setPrevious(previousNode);
        }
        if (previousNode == null) {
            head = node.getNext();
            if (nextNode != null) nextNode.setPrevious(null);
        }
        if (nextNode == null) {
            tail = node.getPrevious();
            if (previousNode != null) previousNode.setNext(null);
        }
    }


    public ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.getValue());
            currentNode = currentNode.getNext();
        }
        return history;
    }
}
