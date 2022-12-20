package solutions.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

    private class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    public int getSolution(List<String> lines) {
        List<Node> orderOfNodes = new ArrayList<>();
        int firstValue = Integer.parseInt(lines.get(0));
        Node firstNode = new Node(firstValue);
        orderOfNodes.add(firstNode);
        Node currentNode = firstNode;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            int value = Integer.parseInt(line);
            Node node = new Node(value);
            orderOfNodes.add(node);
            currentNode.next = node;
            currentNode = node;
        }
        currentNode.next = firstNode;

        for (Node node : orderOfNodes) {
            moveNode(node, orderOfNodes.size());
        }

        printNodes(firstNode);

        Node searchNode = findNode(firstNode, 0);
        int thousandth = 0;
        int twoThousandth = 0;
        int threeThousandth = 0;

        for (int i = 1; i <= 3000; i++) {
            searchNode = searchNode.next;
            if (i == 1000) {
                thousandth = searchNode.value;
            } else if (i == 2000) {
                twoThousandth = searchNode.value;
            } else if (i == 3000) {
                threeThousandth = searchNode.value;
            }
        }
        return thousandth + twoThousandth + threeThousandth;
    }

    private Node findNode(Node node, int value) {
        Node currentNode = node;
        while (currentNode != null) {
            if (currentNode.value == value) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private Node findPreviousNode(Node node) {
        Node previousNode = node;
        while (previousNode != null) {
            if (previousNode.next == node) {
                return previousNode;
            }
            previousNode = previousNode.next;
        }
        return null;
    }

    private void moveNode(Node nodeToMove, int size) {
        // size 7, move -3 means move 3
        int moveCount = Math.floorMod(nodeToMove.value, size - 1);
        Node previousNode = findPreviousNode(nodeToMove);
        for (int i = 0; i < moveCount; i++) {
            Node nextNext = nodeToMove.next.next;
            previousNode.next = nodeToMove.next;
            previousNode = previousNode.next;
            previousNode.next = nodeToMove;
            nodeToMove.next = nextNext;
//            printNodes(nodeToMove);
        }
    }

    private void printNodes(Node startNode) {
        List<Integer> values = new ArrayList<>();
        values.add(startNode.value);
        Node currentNode = startNode.next;
        while (currentNode != startNode) {
            values.add(currentNode.value);
            currentNode = currentNode.next;
        }
        System.out.println(values.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day20input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
