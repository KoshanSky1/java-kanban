package com.practicum.kanban.service;

import com.practicum.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> taskNodeMap = new HashMap<>();
    private Node head = null;
    private Node tail = null;

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
        Node deletingNode = taskNodeMap.remove(taskId);

        removeNode(deletingNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Task task) {
        Node newNode = new Node(task, tail, null);

        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }

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
            return;
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

    private List<Task> getTasks() {
        List<Task> history = new LinkedList<>();
        Node currentNode = head;

        while (currentNode != null) {
            history.add(currentNode.getValue());
            currentNode = currentNode.getNext();
        }

        return history;
    }

    private static class Node {
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

}