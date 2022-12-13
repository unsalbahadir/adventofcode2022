package solutions.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Solution {

    private class Position {
        int row;
        int column;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public int countPositionsTailVisited(List<String> lines) {
        int[][] grid = new int[500][500];

        handleMoves(grid, lines, 250);

        return countVisitedPositions(grid);
    }

    private void handleMoves(int[][] grid, List<String> lines, int startingPosition) {
        grid[startingPosition][startingPosition] = 1; // starting position
        Position headPosition = new Position(startingPosition, startingPosition);
        Position tailPosition = new Position(startingPosition, startingPosition);

        for (String line : lines) {
            String[] move = line.split(" ");
            String direction = move[0];
            int moveAmount = Integer.parseInt(move[1]);

            for (int i = 0; i < moveAmount; i++) {
                moveHead(headPosition, direction);
                boolean headAndTailAdjacent = isHeadAndTailAdjacent(headPosition, tailPosition);
                if (!headAndTailAdjacent) {
                    moveTail(headPosition, tailPosition);
                    grid[tailPosition.row][tailPosition.column] = 1;
                }
            }
        }
    }



    public int countPositionsTailVisited2(List<String> lines) {
        int[][] grid = new int[500][500];

        handleMoves2(grid, lines, 250);

        return countVisitedPositions(grid);
    }

    private int countVisitedPositions(int[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private void handleMoves2(int[][] grid, List<String> lines, int startingPosition) {
        grid[startingPosition][startingPosition] = 1; // starting position
        Position[] knots = new Position[10];
        for (int i = 0; i < knots.length; i++) {
            knots[i] = new Position(startingPosition, startingPosition);
        }
        Position headKnot = knots[0];
        Position tailKnot = knots[9];

        for (String line : lines) {
            String[] move = line.split(" ");
            String direction = move[0];
            int moveAmount = Integer.parseInt(move[1]);

            for (int i = 0; i < moveAmount; i++) {
                moveHead(headKnot, direction);
                for (int currentKnotIndex = 1; currentKnotIndex < knots.length; currentKnotIndex++) {
                    Position currentKnot = knots[currentKnotIndex];
                    Position knotToFollow = knots[currentKnotIndex - 1];
                    boolean headAndTailAdjacent = isHeadAndTailAdjacent(knotToFollow, currentKnot);
                    if (!headAndTailAdjacent) {
                        moveTail(knotToFollow, currentKnot);
                    }
                }
                grid[tailKnot.row][tailKnot.column] = 1;
            }
        }
    }

    private void moveHead(Position headPosition, String direction) {
        if ("U".equals(direction)) {
            headPosition.row--;
        } else if ("R".equals(direction)) {
            headPosition.column++;
        } else if ("D".equals(direction)) {
            headPosition.row++;
        } else if ("L".equals(direction)) {
            headPosition.column--;
        }
    }

    private boolean isHeadAndTailAdjacent(Position headPosition, Position tailPosition) {
        if (headPosition.row == tailPosition.row) {
            return Math.abs(headPosition.column - tailPosition.column) <= 1;
        } else if (headPosition.column == tailPosition.column) {
            return Math.abs(headPosition.row - tailPosition.row) <= 1;
        } else {
            return Math.abs(headPosition.column - tailPosition.column) <= 1 && Math.abs(headPosition.row - tailPosition.row) <= 1;
        }
    }

    private void moveTail(Position headPosition, Position tailPosition) {
        if (headPosition.row == tailPosition.row) {
            if (headPosition.column > tailPosition.column) {
                tailPosition.column++;
            } else {
                tailPosition.column--;
            }
        } else if (headPosition.column == tailPosition.column) {
            if (headPosition.row > tailPosition.row) {
                tailPosition.row++;
            } else {
                tailPosition.row--;
            }
        } else {
            if (headPosition.column > tailPosition.column) {
                tailPosition.column++;
            } else {
                tailPosition.column--;
            }
            if (headPosition.row > tailPosition.row) {
                tailPosition.row++;
            } else {
                tailPosition.row--;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day9input.txt"));
        System.out.println(solution.countPositionsTailVisited(lines));
        System.out.println(solution.countPositionsTailVisited2(lines));
    }
}
