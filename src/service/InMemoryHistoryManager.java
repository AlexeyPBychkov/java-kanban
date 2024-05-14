package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> historyMap;
    private Node last;
    private Node prev;

    private void linkLast(Task task) {
        final Node lastNode = last;
        final Node newNode = new Node(lastNode, task, null);
        last = newNode;
        if (lastNode == null) {
            prev = newNode;
        } else {
            lastNode.next = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    @Override
    public List<Task> getHistoryList() {
        return List.copyOf(getTasks());
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        Node currentNode = prev;
        while (currentNode != null) {
            tasksList.add(currentNode.element);
            currentNode = currentNode.next;
        }
        return tasksList;
    }

    private void removeNode(Node node) {
        if (node.prev == null) {
            if (node.next == null) {
                this.prev = null;
                this.last = null;
            } else {
                node.next.prev = null;
                this.prev = node.next;
            }
        } else {
            node.prev.next = node.next;
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
        historyMap.remove(node.element.getId());
    }

    private static class Node {
        private Node prev;
        private Node next;
        private Task element;

        Node(Node prev, Task element, Node next) {
            this.prev = prev;
            this.element = element;
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Task getElement() {
            return element;
        }

        public void setElement(Task element) {
            this.element = element;
        }
    }

}
