package solutions.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

    private int maxRow;
    private int minRow = 1;
    private int maxColumn;
    private int minColumn = 1;

    public int getSolution(List<String> lines) {
        List<Blizzard> blizzards = buildBlizzards(lines);
//        System.out.println(blizzards);

        Position startingPosition = getStartingPosition(lines);
//        System.out.println(startingPosition);
        Position endingPosition = getEndingPosition(lines);
//        System.out.println(endingPosition);

        maxRow = lines.size() - 2;
        maxColumn = lines.get(0).length() - 2;

        return reachGoal(blizzards, startingPosition, endingPosition);
    }

    public int getSolution2(List<String> lines) {
        List<Blizzard> blizzards = buildBlizzards(lines);
//        System.out.println(blizzards);

        Position startingPosition = getStartingPosition(lines);
//        System.out.println(startingPosition);
        Position endingPosition = getEndingPosition(lines);
//        System.out.println(endingPosition);

        maxRow = lines.size() - 2;
        maxColumn = lines.get(0).length() - 2;

        int firstRun = reachGoal(blizzards, startingPosition, endingPosition);
        System.out.println("First run: " + firstRun);
        int secondRun = reachGoal(blizzards, endingPosition, startingPosition);
        System.out.println("Second run: " + secondRun);
        int thirdRun = reachGoal(blizzards, startingPosition, endingPosition);
        System.out.println("Third run: " + thirdRun);
        return firstRun + secondRun + thirdRun;
    }

    private List<Blizzard> buildBlizzards(List<String> lines) {
        List<Blizzard> blizzards = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.' && c != '#') {
                    Blizzard blizzard = new Blizzard(new Position(i, j), getDirection(c));
                    blizzards.add(blizzard);
                }
            }
        }

        return blizzards;
    }

    private Direction getDirection(char c) {
        return switch (c) {
            case '^' -> Direction.UP;
            case '>' -> Direction.RIGHT;
            case 'v' -> Direction.DOWN;
            case '<' -> Direction.LEFT;
            default -> null;
        };
    }

    private Position getStartingPosition(List<String> lines) {
        String firstLine = lines.get(0);
        for (int i = 0; i < firstLine.length(); i++) {
            char c = firstLine.charAt(i);
            if (c == '.') {
                return new Position(0, i);
            }
        }
        return new Position(-1, -1);
    }

    private Position getEndingPosition(List<String> lines) {
        String lastLine = lines.get(lines.size() - 1);
        for (int i = 0; i < lastLine.length(); i++) {
            char c = lastLine.charAt(i);
            if (c == '.') {
                return new Position(lines.size() - 1, i);
            }
        }
        return new Position(-1, -1);
    }

    private int reachGoal(List<Blizzard> blizzards, Position startingPosition, Position endingPosition) {
        Set<Position> positions = new HashSet<>();
        positions.add(startingPosition);
        int minute = 0;
        while (!positions.isEmpty()) {
            move(blizzards);

            Set<Position> nextPositions = new HashSet<>();
            for (Position currentPosition : positions) {
                List<Position> possiblePositions = getAdjacentPositions(currentPosition);
                possiblePositions.add(currentPosition); // wait
                for (Position possiblePosition : possiblePositions) {
                    if (possiblePosition.equals(endingPosition)) {
                        return minute + 1;
                    }
                    if (canMove(blizzards, possiblePosition, startingPosition)) {
                        nextPositions.add(possiblePosition);
                    }
                }
            }
            positions = nextPositions;
            minute++;
        }
        return minute;
    }

    private void move(List<Blizzard> blizzards) {
        for (Blizzard blizzard : blizzards) {
            move(blizzard);
        }
    }

    private void move(Blizzard blizzard) {
        if (blizzard.direction == Direction.UP) {
            if (blizzard.position.row == minRow) {
                blizzard.position.row = maxRow;
            } else {
                blizzard.position.row--;
            }
        } else if (blizzard.direction == Direction.RIGHT) {
            if (blizzard.position.column == maxColumn) {
                blizzard.position.column = minColumn;
            } else {
                blizzard.position.column++;
            }
        } else if (blizzard.direction == Direction.DOWN) {
            if (blizzard.position.row == maxRow) {
                blizzard.position.row = minRow;
            } else {
                blizzard.position.row++;
            }
        } else if (blizzard.direction == Direction.LEFT) {
            if (blizzard.position.column == minColumn) {
                blizzard.position.column = maxColumn;
            } else {
                blizzard.position.column--;
            }
        }
    }

    private List<Position> getAdjacentPositions(Position currentPosition) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(currentPosition.row - 1, currentPosition.column));
        adjacentPositions.add(new Position(currentPosition.row, currentPosition.column + 1));
        adjacentPositions.add(new Position(currentPosition.row + 1, currentPosition.column));
        adjacentPositions.add(new Position(currentPosition.row, currentPosition.column - 1));

        return adjacentPositions;
    }

    private boolean canMove(List<Blizzard> blizzards, Position proposedPosition, Position startingPosition) {
        if (proposedPosition.equals(startingPosition)) {
            return true;
        }

        Set<Position> blizzardPositions = blizzards.stream()
                .map(blizzard -> blizzard.position)
                .collect(Collectors.toSet());

        return !blizzardPositions.contains(proposedPosition)
                && proposedPosition.row >= minRow
                && proposedPosition.row <= maxRow
                && proposedPosition.column >= minColumn
                && proposedPosition.column <= maxColumn;
    }



    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day24input.txt"));
//        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
