package solutions.day22;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private enum Facing {
        RIGHT,
        DOWN,
        LEFT,
        UP;
    }

    private class Position {
        int row;
        int column;
        Facing facing;

        public Position(int row, int column, Facing facing) {
            this.row = row;
            this.column = column;
            this.facing = facing;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "row=" + (row + 1) +
                    ", column=" + (column + 1) +
                    ", facing=" + facing +
                    '}';
        }
    }

    public int getSolution(List<String> lines) {
        Character[][] grid = buildGrid(lines);
//        for (Character[] characters : grid) {
//            System.out.println(Arrays.toString(characters));
//        }

        String movementLine = lines.get(lines.size() - 1);
        List<String> movements = getMovements(movementLine);
        System.out.println(movements);

        Position startingPosition = getStartingPosition(grid[0]);
        markGrid(grid, startingPosition);
        System.out.println("Start: " + startingPosition);

        int move = 0;
        for (String movement : movements) {
            if (StringUtils.isNumeric(movement)) {
                move(startingPosition, grid, movement);
            } else {
                turn(startingPosition, movement);
                markGrid(grid, startingPosition);
            }
            move++;
            if (move % 500 == 0) {
                System.out.println("Grid after move " + move + ":");
                printGrid(grid);
            }
        }

        System.out.println("End: " + startingPosition);

        printGrid(grid);
        return ((startingPosition.row + 1) * 1000) + ((startingPosition.column + 1) * 4) + startingPosition.facing.ordinal();
    }

    private static void printGrid(Character[][] grid) {
        for (Character[] characters : grid) {
            for (Character character : characters) {
                if (character == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(character);
                }
            }
            System.out.println();
        }
    }

    private Character[][] buildGrid(List<String> lines) {
        Character[][] grid = new Character[lines.size() - 2][];
        for (int i = 0; i < lines.size() - 2; i++) {
            String line = lines.get(i);
            grid[i] = new Character[line.length()];
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (!Character.isWhitespace(c)) {
                    grid[i][j] = c;
                }
            }
        }

        return grid;
    }

    private List<String> getMovements(String movementLine) {
        List<String> movements = new ArrayList<>();
        StringBuilder lastNumber = new StringBuilder();
        for (int i = 0; i < movementLine.length(); i++) {
            char c = movementLine.charAt(i);
            if (Character.isDigit(c)) {
                lastNumber.append(c);
            } else {
                movements.add(lastNumber.toString());
                movements.add(String.valueOf(c));
                lastNumber = new StringBuilder();
            }
        }
        movements.add(lastNumber.toString());
        return movements;
    }

    private Position getStartingPosition(Character[] characters) {
        for (int i = 0; i < characters.length; i++) {
            Character character = characters[i];
            if (character != null) {
                return new Position(0, i, Facing.RIGHT);
            }
        }
        return new Position(0, 0, Facing.RIGHT);
    }

    private void move(Position currentPosition, Character[][] grid, String movement) {
        int moveCount = Integer.parseInt(movement);
        for (int i = 0; i < moveCount; i++) {
            if (currentPosition.facing == Facing.RIGHT) {
                Character[] currentRow = grid[currentPosition.row];
                int nextColumn;
                if (currentPosition.column == currentRow.length - 1) {
                    // wrap around
                    nextColumn = getLeftmostIndexInRow(currentRow);
                } else {
                    nextColumn = currentPosition.column + 1;
                }
                if (currentRow[nextColumn] == '#') {
                    // stop
                    return;
                }
                currentPosition.column = nextColumn;

            } else if (currentPosition.facing == Facing.DOWN) {
                int nextRow;
                int currentColumn = currentPosition.column;
                if (currentPosition.row == getBottomIndexInColumn(grid, currentColumn)) {
                    // wrap around
                    nextRow = getTopIndexInColumn(grid, currentColumn);
                } else {
                    nextRow = currentPosition.row + 1;
                }
                if (grid[nextRow][currentColumn] == '#') {
                    // stop
                    return;
                }
                currentPosition.row = nextRow;

            } else if (currentPosition.facing == Facing.LEFT) {
                Character[] currentRow = grid[currentPosition.row];
                int nextColumn;
                if (currentPosition.column == getLeftmostIndexInRow(currentRow)) {
                    // wrap around
                    nextColumn = currentRow.length - 1;
                } else {
                    nextColumn = currentPosition.column - 1;
                }
                if (currentRow[nextColumn] == '#') {
                    // stop
                    return;
                }
                currentPosition.column = nextColumn;

            } else {
                int nextRow;
                int currentColumn = currentPosition.column;
                if (currentPosition.row == getTopIndexInColumn(grid, currentColumn)) {
                    // wrap around
                    nextRow = getBottomIndexInColumn(grid, currentColumn);
                } else {
                    nextRow = currentPosition.row - 1;
                }
                if (grid[nextRow][currentColumn] == '#') {
                    // stop
                    return;
                }
                currentPosition.row = nextRow;
            }
            markGrid(grid, currentPosition);
        }
    }

    private int getLeftmostIndexInRow(Character[] characters) {
        for (int i = 0; i < characters.length; i++) {
            Character character = characters[i];
            if (character != null) {
                return i;
            }
        }
        return -1;
    }

    private int getTopIndexInColumn(Character[][] grid, int currentColumn) {
        for (int i = 0; i < grid.length; i++) {
            Character[] row = grid[i];
            if (row[currentColumn] != null) {
                return i;
            }
        }
        return -1;
    }

    private int getBottomIndexInColumn(Character[][] grid, int currentColumn) {
        for (int i = grid.length - 1; i >= 0; i--) {
            Character[] row = grid[i];
            if (row.length > currentColumn && row[currentColumn] != null) {
                return i;
            }
        }
        return -1;
    }

    private void turn(Position currentPosition, String turnDirection) {
        Facing currentFacing = currentPosition.facing;
        Facing nextFacing;
        if (turnDirection.equals("L")) {
            if (currentFacing == Facing.RIGHT) {
                nextFacing = Facing.UP;
            } else if (currentFacing == Facing.DOWN) {
                nextFacing = Facing.RIGHT;
            } else if (currentFacing == Facing.LEFT) {
                nextFacing = Facing.DOWN;
            } else {
                nextFacing = Facing.LEFT;
            }
        } else {
            if (currentFacing == Facing.RIGHT) {
                nextFacing = Facing.DOWN;
            } else if (currentFacing == Facing.DOWN) {
                nextFacing = Facing.LEFT;
            } else if (currentFacing == Facing.LEFT) {
                nextFacing = Facing.UP;
            } else {
                nextFacing = Facing.RIGHT;
            }
        }
        currentPosition.facing = nextFacing;
    }

    private void markGrid(Character[][] grid, Position currentPosition) {
        grid[currentPosition.row][currentPosition.column] = getCharacterForFacing(currentPosition.facing);
    }

    private Character getCharacterForFacing(Facing facing) {
        return switch (facing) {
            case RIGHT -> '>';
            case DOWN -> 'v';
            case LEFT -> '<';
            default -> '^';
        };
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day22input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
