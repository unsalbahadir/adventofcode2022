package solutions.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private int targetRow = 10;

    private class Position {
        int row;
        int column;
        int distanceToClosestBeacon;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public int getSolution(List<String> lines) {
        char[][] grid = buildGrid(lines);

        for (char[] chars : grid) {
            System.out.println(chars);
        }

        int count = 0;
        for (char c : grid[targetRow]) {
            if (c == '#') {
                count++;
            }
        }

        return count;
    }

    private char[][] buildGrid(List<String> lines) {
        List<Position> sensorPositions = new ArrayList<>();
        List<Position> beaconPositions = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split(":");
            String[] sensorSplit = split[0].split(" ");
            String[] beaconSplit = split[1].split(" ");

            Position sensorPosition = new Position(getY(sensorSplit[3]), getX(sensorSplit[2]));
            Position beaconPosition = new Position(getY(beaconSplit[6]), getX(beaconSplit[5]));

            sensorPosition.distanceToClosestBeacon = getManhattanDistance(sensorPosition, beaconPosition);

            sensorPositions.add(sensorPosition);
            beaconPositions.add(beaconPosition);
        }

        List<Position> allPositions = Stream.concat(sensorPositions.parallelStream(), beaconPositions.parallelStream())
                .collect(Collectors.toList());
        normalizeCoordinates(allPositions);

        char[][] grid = createGridFromMaxValues(allPositions);

        draw(grid, sensorPositions, beaconPositions);

        return grid;
    }

    private int getManhattanDistance(Position firstPosition, Position secondPosition) {
        return Math.abs(firstPosition.row - secondPosition.row) + Math.abs(firstPosition.column - secondPosition.column);
    }

    private int getX(String s) {
        int start = s.indexOf("=");
        int end = s.indexOf(",");

        return Integer.parseInt(s.substring(start + 1, end));
    }

    private int getY(String s) {
        int start = s.indexOf("=");

        return Integer.parseInt(s.substring(start + 1));
    }

    private void normalizeCoordinates(List<Position> positions) {
        int minRow = Integer.MAX_VALUE;
        int minColumn = Integer.MAX_VALUE;

        // find min values
        for (Position position : positions) {
            minRow = Math.min(minRow, position.row);
            minColumn = Math.min(minColumn, position.column);
        }

        // subtract min from every position
        for (Position position : positions) {
            position.row -= minRow;
            position.column -= minColumn;
        }
        targetRow -= minRow;
    }

    private char[][] createGridFromMaxValues(List<Position> positions) {
        int maxRow = Integer.MIN_VALUE;
        int maxColumn = Integer.MIN_VALUE;

        for (Position position : positions) {
            maxRow = Math.max(maxRow, position.row);
            maxColumn = Math.max(maxColumn, position.column);
        }

        char[][] grid = new char[maxRow + 1][maxColumn + 1];
        for (char[] chars : grid) {
            Arrays.fill(chars, '.');
        }
        return grid;
    }

    private void draw(char[][] grid, List<Position> sensorPositions, List<Position> beaconPositions) {
        for (Position sensorPosition : sensorPositions) {
            grid[sensorPosition.row][sensorPosition.column] = 'S';
            drawEmptyPositions(grid, sensorPosition);
        }

        for (Position beaconPosition : beaconPositions) {
            grid[beaconPosition.row][beaconPosition.column] = 'B';
        }
    }

    private void drawEmptyPositions(char[][] grid, Position sensorPosition) {
        int distanceToClosestBeacon = sensorPosition.distanceToClosestBeacon;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Position currentPosition = new Position(i, j);
                if (getManhattanDistance(currentPosition, sensorPosition) <= distanceToClosestBeacon) {
                    grid[i][j] = '#';
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day15input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
