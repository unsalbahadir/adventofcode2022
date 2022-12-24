package solutions.day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public static final int MIN_COLUMN = 0;
    public static final int MAX_COLUMN = 6;

    public long getSolution(List<String> lines) {
        List<Character> jetPatterns = new ArrayList<>();
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                jetPatterns.add(c);
            }
        }

        return run(2022, jetPatterns) + 1;
    }

    public long getSolution2(List<String> lines) {
        List<Character> jetPatterns = new ArrayList<>();
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                jetPatterns.add(c);
            }
        }

        return run(1000000000000L, jetPatterns) + 1;
    }

    private long run(long rockLimit, List<Character> jetPatterns) {
        SetWithMaxSize allPositions = new SetWithMaxSize(200);
        Map<Long, Long> heightAddedAtShapeCount = new HashMap<>();

        int jetIndex = 0;
        long shapeCount = 0;
        int shapeType = 0;
        long shapeCountSinceLastReset = 0;
        int resetCount = 0;
        long maxRowInLastPattern = 0;
        int patternCount = 0;
        while (shapeCount < rockLimit) {
            Shape newShape = getShape(getHighestRow(allPositions) + 4, 2, shapeType);
            shapeType++;
            if (shapeType >= 5) {
                shapeType = 0;
            }
            boolean stopped = false;
            while (!stopped) {
                pushRock(allPositions.getElements(), newShape, jetPatterns.get(jetIndex));
                jetIndex++;
                if (jetIndex == jetPatterns.size()) {
                    jetIndex = 0;
                    resetCount++;
                    if (resetCount == 5) {
                        resetCount = 0;
                        long heightChangeInPattern = getHighestRow(allPositions) - maxRowInLastPattern;
                        patternCount++;
                        if (patternCount == 2) {
                            long remainingRocks = rockLimit - shapeCount;
                            long neededPatternRounds = remainingRocks / shapeCountSinceLastReset;
                            long remainderShapeCountAfterPattern = remainingRocks % shapeCountSinceLastReset;
                            Long remainderHeight = heightAddedAtShapeCount.get(remainderShapeCountAfterPattern);
                            return getHighestRow(allPositions) + (neededPatternRounds * heightChangeInPattern) + remainderHeight;
                        }
                        shapeCountSinceLastReset = 0;
                        maxRowInLastPattern = getHighestRow(allPositions);
                    }
                }

                // check if new positions clash with existing shape positions
                moveShapeDown(newShape);
                if (isClashing(allPositions.getElements(), newShape) || getBottom(newShape) == -1) {
                    moveShapeUp(newShape);
                    stopped = true;
                }
            }
            allPositions.addAll(newShape.positions);
            shapeCount++;
            shapeCountSinceLastReset++;
            long heightChangeSinceReset = getHighestRow(allPositions) - maxRowInLastPattern;
            heightAddedAtShapeCount.put(shapeCountSinceLastReset, heightChangeSinceReset);
        }

        return getHighestRow(allPositions);
    }

    private void pushRock(Set<Position> positions, Shape currentShape, Character jetPattern) {
        if (jetPattern == '>') {
            Position rightmostPosition = getRightmostPosition(currentShape);
            if (rightmostPosition.column != MAX_COLUMN) {
                moveShapeRight(currentShape);
                if (isClashing(positions, currentShape)) { // revert
                    moveShapeLeft(currentShape);
                }
            }
        } else {
            Position leftmostPosition = getLeftmostPosition(currentShape);
            if (leftmostPosition.column != MIN_COLUMN) {
                moveShapeLeft(currentShape);
                if (isClashing(positions, currentShape)) {
                    moveShapeRight(currentShape);
                }
            }
        }
    }

    private boolean isClashing(Set<Position> positions, Shape newShape) {
        return newShape.positions.stream().anyMatch(positions::contains);
    }

    private Position getRightmostPosition(Shape shape) {
        int rightmostColumn = MIN_COLUMN - 1;
        Position rightmostPosition = null;
        for (Position position : shape.positions) {
            if (position.column > rightmostColumn) {
                rightmostColumn = position.column;
                rightmostPosition = position;
            }
        }
        return rightmostPosition;
    }

    private Position getLeftmostPosition(Shape shape) {
        int leftmostColumn = MAX_COLUMN + 1;
        Position leftmostPosition = null;
        for (Position position : shape.positions) {
            if (position.column < leftmostColumn) {
                leftmostColumn = position.column;
                leftmostPosition = position;
            }
        }
        return leftmostPosition;
    }

    private long getHighestRow(List<Shape> shapes) {
        long maxRow = -1;
        for (Shape shape : shapes) {
            for (Position position : shape.positions) {
                maxRow = Math.max(maxRow, position.row);
            }
        }
        return maxRow;
    }

    private long getHighestRow(SetWithMaxSize positions) {
        if (positions.getElements().isEmpty()) {
            return -1;
        }
        return positions.getLast().row;
    }

    private long getBottom(Shape shape) {
        long minRow = Integer.MAX_VALUE;
        for (Position position : shape.positions) {
            minRow = Math.min(minRow, position.row);
        }
        return minRow;
    }

    private void moveShapeRight(Shape shape) {
        for (Position position : shape.positions) {
            position.column++;
        }
    }

    private void moveShapeLeft(Shape shape) {
        for (Position position : shape.positions) {
            position.column--;
        }
    }

    private void moveShapeDown(Shape shape) {
        for (Position position : shape.positions) {
            position.row--;
        }
    }

    private void moveShapeUp(Shape shape) {
        for (Position position : shape.positions) {
            position.row++;
        }
    }

    private Shape getShape(long bottomRow, int leftEdgeColumn, long turn) {
        turn = turn % 5;

        Shape shape;
        if (turn == 0) {
            shape = getHorizontalLineShape(bottomRow, leftEdgeColumn);
        } else if (turn == 1) {
            shape = getPlusShape(bottomRow, leftEdgeColumn);
        } else if (turn == 2) {
            shape = getLShape(bottomRow, leftEdgeColumn);
        } else if (turn == 3) {
            shape = getVerticalLineShape(bottomRow, leftEdgeColumn);
        } else {
            shape = getSquareShape(bottomRow, leftEdgeColumn);
        }
        return shape;
    }

    private Shape getHorizontalLineShape(long bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 3));
        return shape;
    }

    private Shape getPlusShape(long bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow + 2, leftEdgeColumn + 1));
        return shape;
    }

    private Shape getLShape(long bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow + 2, leftEdgeColumn + 2));
        return shape;
    }

    private Shape getVerticalLineShape(long bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 2, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 3, leftEdgeColumn));
        return shape;
    }

    private Shape getSquareShape(long bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 1));
        return shape;
    }

    private void printShapes(List<Shape> shapes) {
        Set<Position> positions = shapes.stream().flatMap(shape -> shape.positions.stream()).collect(Collectors.toSet());

        long highestRow = getHighestRow(shapes);
        char[][] grid = new char[(int) (highestRow  + 1)][7];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (positions.contains(new Position(i, j))) {
                    grid[i][j] = '#';
                } else {
                    grid[i][j] = '.';
                }
            }
        }

        for (int j = grid.length - 1; j >= 0; j--) {
            char[] characters = grid[j];
            System.out.print('|');
            for (char character : characters) {
                System.out.print(character);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("+-------+");
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day17input.txt"));
//        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
