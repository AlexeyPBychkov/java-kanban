package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Node prev;
        Node next;
        Task element;

        Node(Node prev, Task element, Node next) {
            this.prev = prev;
            this.element = element;
            this.next = next;
        }
    }

    HashMap<Integer, Node> historyMap;
    Node last;
    Node prev;

    public InMemoryHistoryManager(){
        historyMap = new HashMap<>();
    }

    @Override
    public List<Task> getHistoryList() {
        return List.copyOf(getTasks());
    }

    @Override
    public void add(Task task) {
        if(task == null) {
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

    void linkLast(Task task) {
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

    ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        Node currentNode = prev;
        while (currentNode != null) {
            tasksList.add(currentNode.element);
            currentNode = currentNode.next;
        }
        return tasksList;
    }

    void removeNode(Node node) {
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

}
