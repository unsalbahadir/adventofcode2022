package solutions.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

    public int getSolution(List<String> lines) {
        char[][] grid = buildGrid(lines);

//        for (char[] chars : grid) {
//            System.out.println(chars);
//        }

        return simulateSand(grid);
    }

    private char[][] buildGrid(List<String> lines) {
        char[][] grid = new char[200][600];
        for (char[] chars : grid) {
            Arrays.fill(chars, '.');
        }

        for (String line : lines) {
            String[] rockCoordinates = line.split("->");
            for (int i = 1; i < rockCoordinates.length; i++) {
                String previousRockCoordinate = rockCoordinates[i - 1].trim();
                String rockCoordinate = rockCoordinates[i].trim();
                drawRocks(grid, previousRockCoordinate, rockCoordinate);
            }
        }
        grid[0][500] = '+';

        return grid;
    }

    private void drawRocks(char[][] grid, String firstRockCoordinate, String secondRockCoordinate) {
        String[] firstSplit = firstRockCoordinate.split(",");
        int firstColumn = Integer.parseInt(firstSplit[0]);
        int firstRow = Integer.parseInt(firstSplit[1]);

        String[] secondSplit = secondRockCoordinate.split(",");
        int secondColumn = Integer.parseInt(secondSplit[0]);
        int secondRow = Integer.parseInt(secondSplit[1]);

        if (firstColumn == secondColumn) {
            int smallerValue = Math.min(firstRow, secondRow);
            int largerValue = Math.max(firstRow, secondRow);
            for (int i = smallerValue; i <= largerValue; i++) {
                grid[i][firstColumn] = '#';
            }
        } else {
            int smallerValue = Math.min(firstColumn, secondColumn);
            int largerValue = Math.max(firstColumn, secondColumn);
            for (int i = smallerValue; i <= largerValue; i++) {
                grid[firstRow][i] = '#';
            }
        }
    }

    private int simulateSand(char[][] grid) {
        int unitsOfSand = 0;

        Position startingPosition = new Position(0, 500);

        Position sandPosition = new Position(startingPosition.row, startingPosition.column);
        while (sandPosition.row < grid.length - 1) {
            if (grid[sandPosition.row + 1][sandPosition.column] == '.') {
                sandPosition.row++;
            } else if (grid[sandPosition.row + 1][sandPosition.column - 1] == '.') {
                sandPosition.row++;
                sandPosition.column--;
            } else if (grid[sandPosition.row + 1][sandPosition.column + 1] == '.') {
                sandPosition.row++;
                sandPosition.column++;
            } else { // comes to rest
                grid[sandPosition.row][sandPosition.column] = 'o';
                sandPosition = new Position(startingPosition.row, startingPosition.column);
                unitsOfSand++;
            }
        }

        return unitsOfSand;
    }

    public int getSolution2(List<String> lines) {
        char[][] grid = buildGrid2(lines);

//        for (char[] chars : grid) {
//            System.out.println(chars);
//        }

        return simulateSand2(grid);
    }

    private char[][] buildGrid2(List<String> lines) {
        char[][] grid = new char[200][700];
        for (char[] chars : grid) {
            Arrays.fill(chars, '.');
        }

        int highestRockRow = 0;
        for (String line : lines) {
            String[] rockCoordinates = line.split("->");
            for (int i = 1; i < rockCoordinates.length; i++) {
                String previousRockCoordinate = rockCoordinates[i - 1].trim();
                String rockCoordinate = rockCoordinates[i].trim();
                drawRocks(grid, previousRockCoordinate, rockCoordinate);

                highestRockRow = Math.max(highestRockRow, Integer.parseInt(rockCoordinate.split(",")[1]));
            }
        }
        grid[0][500] = '+';
        Arrays.fill(grid[highestRockRow + 2], '#');

        return grid;
    }

    private int simulateSand2(char[][] grid) {
        int unitsOfSand = 0;

        Position startingPosition = new Position(0, 500);

        Position sandPosition = new Position(startingPosition.row, startingPosition.column);
        while (sandPosition.row < grid.length - 1) {
            if (grid[sandPosition.row + 1][sandPosition.column] == '.') {
                sandPosition.row++;
            } else if (grid[sandPosition.row + 1][sandPosition.column - 1] == '.') {
                sandPosition.row++;
                sandPosition.column--;
            } else if (grid[sandPosition.row + 1][sandPosition.column + 1] == '.') {
                sandPosition.row++;
                sandPosition.column++;
            } else { // comes to rest
                if (sandPosition.row == 0 && sandPosition.column == 500) {
                    // source blocked
                    grid[sandPosition.row][sandPosition.column] = 'o';
                    unitsOfSand++;
                    break;
                }
                grid[sandPosition.row][sandPosition.column] = 'o';
                sandPosition = new Position(startingPosition.row, startingPosition.column);
                unitsOfSand++;
            }
        }

        return unitsOfSand;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day14input.txt"));
//        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
