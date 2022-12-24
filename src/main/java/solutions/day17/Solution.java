package solutions.day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

    public static final int MIN_COLUMN = 0;
    public static final int MAX_COLUMN = 6;

    public int getSolution(List<String> lines) {
        List<Character> jetPatterns = new ArrayList<>();
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                jetPatterns.add(c);
            }
        }

        List<Shape> shapes = run(2022, jetPatterns);
        printShapes(shapes);
        return getHighestRow(shapes) + 1;
    }

    private List<Shape> run(int rockLimit, List<Character> jetPatterns) {
        List<Shape> shapes = new ArrayList<>();

        int jetIndex = 0;
        while (shapes.size() < rockLimit) {
            Shape newShape = getShape(getHighestRow(shapes) + 4, 2, shapes.size());
            boolean stopped = false;
            while (!stopped) {
                pushRock(shapes, newShape, jetPatterns.get(jetIndex));
                jetIndex++;
                if (jetIndex == jetPatterns.size()) {
                    jetIndex = 0;
                }

                // check if new positions clash with existing shape positions
                moveShapeDown(newShape);
                if (isClashing(shapes, newShape) || getBottom(newShape) == -1) {
                    moveShapeUp(newShape);
                    stopped = true;
                }
            }
            shapes.add(newShape);
//            printShapes(shapes);
        }

        return shapes;
    }

    private void pushRock(List<Shape> shapes, Shape currentShape, Character jetPattern) {
        if (jetPattern == '>') {
            Position rightmostPosition = getRightmostPosition(currentShape);
            if (rightmostPosition.column != MAX_COLUMN) {
                moveShapeRight(currentShape);
                if (isClashing(shapes, currentShape)) { // revert
                    moveShapeLeft(currentShape);
                }
            }
        } else {
            Position leftmostPosition = getLeftmostPosition(currentShape);
            if (leftmostPosition.column != MIN_COLUMN) {
                moveShapeLeft(currentShape);
                if (isClashing(shapes, currentShape)) {
                    moveShapeRight(currentShape);
                }
            }
        }
    }

    private boolean isClashing(List<Shape> shapes, Shape newShape) {
        Set<Position> positions = shapes.stream().flatMap(shape -> shape.positions.stream()).collect(Collectors.toSet());
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

    private int getHighestRow(List<Shape> shapes) {
        int maxRow = -1;
        for (Shape shape : shapes) {
            for (Position position : shape.positions) {
                maxRow = Math.max(maxRow, position.row);
            }
        }
        return maxRow;
    }

    private int getBottom(Shape shape) {
        int minRow = Integer.MAX_VALUE;
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

    private Shape getShape(int bottomRow, int leftEdgeColumn, int turn) {
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

    private Shape getHorizontalLineShape(int bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 3));
        return shape;
    }

    private Shape getPlusShape(int bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow + 2, leftEdgeColumn + 1));
        return shape;
    }

    private Shape getLShape(int bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 2));
        shape.positions.add(new Position(bottomRow + 2, leftEdgeColumn + 2));
        return shape;
    }

    private Shape getVerticalLineShape(int bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 2, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 3, leftEdgeColumn));
        return shape;
    }

    private Shape getSquareShape(int bottomRow, int leftEdgeColumn) {
        Shape shape = new Shape();
        shape.positions.add(new Position(bottomRow, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow, leftEdgeColumn + 1));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn));
        shape.positions.add(new Position(bottomRow + 1, leftEdgeColumn + 1));
        return shape;
    }

    private void printShapes(List<Shape> shapes) {
        Set<Position> positions = shapes.stream().flatMap(shape -> shape.positions.stream()).collect(Collectors.toSet());

        int highestRow = getHighestRow(shapes);
        char[][] grid = new char[highestRow  + 1][7];
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
        System.out.println(solution.getSolution(lines));
    }
}
