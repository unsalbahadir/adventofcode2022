package solutions.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {

    private class Node {
        int row;
        int column;
        int distanceFromStart;

        public Node(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public Node(int row, int column, int distanceFromStart) {
            this.row = row;
            this.column = column;
            this.distanceFromStart = distanceFromStart;
        }
    }

    public int findShortestPath(List<String> lines) {
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        Node start = null;
        Node end = null;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == 'S') {
                    grid[i][j] = 0;
                    start = new Node(i, j);
                } else if (c == 'E') {
                    grid[i][j] = 25;
                    end = new Node(i, j);
                } else {
                    grid[i][j] = c - 97;
                }
            }
        }

        return findShortestPath(grid, start, end);
    }

    public int findShortestPath2(List<String> lines) {
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        Node end = null;

        List<Node> allStarts = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == 'S') {
                    grid[i][j] = 0;
                } else if (c == 'E') {
                    grid[i][j] = 25;
                    end = new Node(i, j);
                } else {
                    grid[i][j] = c - 97;
                }

                if (grid[i][j] == 0) {
                    allStarts.add(new Node(i, j));
                }
            }
        }

        int shortestPath = Integer.MAX_VALUE;
        for (Node possibleStart : allStarts) {
            int currentShortestPath = findShortestPath(grid, possibleStart, end);
            if (currentShortestPath != -1) {
                shortestPath = Math.min(shortestPath, currentShortestPath);
            }
        }

        return shortestPath;
    }

    private int findShortestPath(int[][] grid, Node start, Node end) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(start);

        boolean[][] visitedNodes = new boolean[grid.length][grid[0].length];
        visitedNodes[start.row][start.column] = true;

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (currentNode.row == end.row && currentNode.column == end.column) {
                return currentNode.distanceFromStart;
            }

            List<Node> validAdjacentNodes = getValidAdjacentNodes(grid, currentNode, visitedNodes);
            queue.addAll(validAdjacentNodes);

        }
        return -1;
    }

    private List<Node> getValidAdjacentNodes(int[][] grid, Node currentNode, boolean[][] visitedNodes) {
        List<Node> validAdjacentNodes = new ArrayList<>();
        int rowLength = grid.length;
        int columnLength = grid[0].length;

        int currentNodeHeight = grid[currentNode.row][currentNode.column];

        // right
        if (currentNode.column + 1 < columnLength &&
                grid[currentNode.row][currentNode.column + 1] <= currentNodeHeight + 1 &&
                !visitedNodes[currentNode.row][currentNode.column + 1]) {
            validAdjacentNodes.add(new Node(currentNode.row, currentNode.column + 1, currentNode.distanceFromStart + 1));
            visitedNodes[currentNode.row][currentNode.column + 1] = true;
        }

        // down
        if (currentNode.row + 1 < rowLength &&
                grid[currentNode.row + 1][currentNode.column] <= currentNodeHeight + 1 &&
                !visitedNodes[currentNode.row + 1][currentNode.column]) {
            validAdjacentNodes.add(new Node(currentNode.row + 1, currentNode.column, currentNode.distanceFromStart + 1));
            visitedNodes[currentNode.row + 1][currentNode.column] = true;
        }

        // left
        if (currentNode.column - 1 >= 0 &&
                grid[currentNode.row][currentNode.column - 1] <= currentNodeHeight + 1 &&
                !visitedNodes[currentNode.row][currentNode.column - 1]) {
            validAdjacentNodes.add(new Node(currentNode.row, currentNode.column - 1, currentNode.distanceFromStart + 1));
            visitedNodes[currentNode.row][currentNode.column - 1] = true;
        }

        // top
        if (currentNode.row - 1 >= 0 &&
                grid[currentNode.row - 1][currentNode.column] <= currentNodeHeight + 1 &&
                !visitedNodes[currentNode.row - 1][currentNode.column]) {
            validAdjacentNodes.add(new Node(currentNode.row - 1, currentNode.column, currentNode.distanceFromStart + 1));
            visitedNodes[currentNode.row - 1][currentNode.column] = true;
        }

        return validAdjacentNodes;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day12input.txt"));
//        System.out.println(solution.findShortestPath(lines));
        System.out.println(solution.findShortestPath2(lines));
    }
}
