package solutions.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    private final int targetRow = 2000000;
    private final Set<Position> emptyPositions = new HashSet<>();

    private final List<Position> sensorPositions = new ArrayList<>();
    private final List<Position> beaconPositions = new ArrayList<>();

    private class Position {
        int row;
        int column;
        int distanceToClosestBeacon;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && column == position.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "row=" + row +
                    ", column=" + column +
                    '}';
        }
    }

    public int getSolution(List<String> lines) {
        build(lines);
        addEmptyPositions(sensorPositions);
        remove();

        List<Position> emptySize = emptyPositions.stream()
                .filter(position -> position.row == targetRow)
                .collect(Collectors.toList());
        return emptySize.size();
    }

    private void build(List<String> lines) {
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

    private void addEmptyPositions(List<Position> sensorPositions) {
        for (Position sensorPosition : sensorPositions) {
            addEmptyPositions(sensorPosition);
        }
    }

    private void addEmptyPositions(Position sensorPosition) {
        int distanceToClosestBeacon = sensorPosition.distanceToClosestBeacon;
        int distanceLeft = distanceToClosestBeacon - Math.abs(targetRow - sensorPosition.row);
        for (int j = 0; j <= distanceLeft; j++) {
            Position emptyPosition = new Position(targetRow, sensorPosition.column + j);
            emptyPositions.add(emptyPosition);
        }
        for (int j = 1; j <= distanceLeft; j++) {
            Position emptyPosition = new Position(targetRow, sensorPosition.column - j);
            emptyPositions.add(emptyPosition);
        }
    }

    private void remove() {
        for (Position sensorPosition : sensorPositions) {
            emptyPositions.remove(sensorPosition);
        }
        for (Position beaconPosition : beaconPositions) {
            emptyPositions.remove(beaconPosition);
        }
    }

    public long getSolution2(List<String> lines) {
        build(lines);
        Position beaconPosition = searchBeacon();

        if (beaconPosition == null) {
            return -1;
        }
        return (beaconPosition.column * 4000000L) + beaconPosition.row;
    }

    private Position searchBeacon() {
        for (Position sensorPosition : sensorPositions) {
            // get fringe positions on each row
            int distanceToClosestBeacon = sensorPosition.distanceToClosestBeacon;
            for (int i = -distanceToClosestBeacon; i <= distanceToClosestBeacon; i++) {
                int row = sensorPosition.row + i;
                if (row < 0 || row > 4000000) {
                    continue;
                }
                int horizontalDistanceLeft = distanceToClosestBeacon - Math.abs(i);
                Position rightmostPosition = new Position(row, sensorPosition.column + horizontalDistanceLeft + 1);
                Position leftmostPosition = new Position(row, sensorPosition.column - horizontalDistanceLeft - 1);

                Position position = checkPositions(List.of(rightmostPosition, leftmostPosition));
                if (position != null) {
                    return position;
                }
            }
        }

        return null;
    }

    private Position checkPositions(List<Position> positions) {
        for (Position position : positions) {
            if (isTargetBeacon(position)) {
                return position;
            }
        }
        return null;
    }

    private boolean isTargetBeacon(Position position) {
        int limit = 4000000;
        return position.row >= 0 && position.column >= 0
                && position.row <= limit && position.column <= limit
                && !sensorPositions.contains(position)
                && !beaconPositions.contains(position)
                && notInDistanceOfAnySensor(position);
    }

    private boolean notInDistanceOfAnySensor(Position position) {
        return sensorPositions.stream()
                .noneMatch(sensorPosition -> isInDistanceToSensor(position, sensorPosition));
    }

    private boolean isInDistanceToSensor(Position currentPosition, Position sensorPosition) {
        return getManhattanDistance(currentPosition, sensorPosition) <= sensorPosition.distanceToClosestBeacon;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day15input.txt"));
//        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
