package solutions.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public int getSolution(List<String> lines) {
        List<Elf> elves = getElves(lines);
//        System.out.println(elves);

        doRounds(elves);
        return findEmptyPositionCount(elves);
    }

    private List<Elf> getElves(List<String> lines) {
        List<Elf> elves = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    Elf elf = new Elf();
                    elf.currentPosition = new Position(i, j);
                    elves.add(elf);
                }
            }
        }
        return elves;
    }

    private void doRounds(List<Elf> elves) {
        Queue<Direction> directions = new LinkedList<>(Arrays.asList(Direction.values()));

        boolean elvesMove = true;
        int round = 0;
        while (round < 10) {
            elvesMove = false;
            // first half
            Set<Position> elfPositions = getAllElfPositions(elves);
            Map<Position, Integer> proposedPositions = new HashMap<>();
            for (Elf elf : elves) {
                Position currentPosition = elf.currentPosition;
                boolean isThereAnElfAdjacent = isThereAnElfAdjacent(elfPositions, currentPosition);
                if (!isThereAnElfAdjacent) {
                    continue;
                }
                for (Direction direction : directions) {
                    if (direction == Direction.NORTH) {
                        boolean adjacentElfInDirection = isThereAnElfAdjacent(elfPositions, currentPosition, "N", "NE", "NW");
                        if (!adjacentElfInDirection) {
                            elf.proposedPosition = getAdjacentPosition(currentPosition, "N");
                            break;
                        }
                    } else if (direction == Direction.SOUTH) {
                        boolean adjacentElfInDirection = isThereAnElfAdjacent(elfPositions, currentPosition, "S", "SE", "SW");
                        if (!adjacentElfInDirection) {
                            elf.proposedPosition = getAdjacentPosition(currentPosition, "S");
                            break;
                        }
                    } else if (direction == Direction.WEST) {
                        boolean adjacentElfInDirection = isThereAnElfAdjacent(elfPositions, currentPosition, "W", "NW", "SW");
                        if (!adjacentElfInDirection) {
                            elf.proposedPosition = getAdjacentPosition(currentPosition, "W");
                            break;
                        }
                    } else {
                        boolean adjacentElfInDirection = isThereAnElfAdjacent(elfPositions, currentPosition, "E", "NE", "SE");
                        if (!adjacentElfInDirection) {
                            elf.proposedPosition = getAdjacentPosition(currentPosition, "E");
                            break;
                        }
                    }
                }
                proposedPositions.merge(elf.proposedPosition, 1, Integer::sum);
            }

            // second half
            for (Elf elf : elves) {
                if (elf.proposedPosition != null) {
                    if (proposedPositions.get(elf.proposedPosition) == 1) {
                        elf.currentPosition = elf.proposedPosition;
                        elvesMove = true;
                    }
                    elf.proposedPosition = null;
                }
            }
            directions.add(directions.poll());
            round++;
        }
    }

    private boolean isThereAnElfAdjacent(Set<Position> positions, Position currentPosition) {
        return isThereAnElfAdjacent(positions, currentPosition, "N", "NE", "NE", "E", "SE", "S", "SW", "W", "NW");
    }

    private boolean isThereAnElfAdjacent(Set<Position> positions, Position currentPosition, String... directions) {
        for (String direction : directions) {
            if (positions.contains(getAdjacentPosition(currentPosition, direction))) {
                return true;
            }
        }
        return false;
    }

    private Position getAdjacentPosition(Position position, String direction) {
        Position adjacentPosition = new Position(position.row, position.column);
        if (direction.contains("N")) {
            adjacentPosition.row--;
        } else if (direction.contains("S")) {
            adjacentPosition.row++;
        }

        if (direction.contains("E")) {
            adjacentPosition.column++;
        } else if (direction.contains("W")) {
            adjacentPosition.column--;
        }
        return adjacentPosition;
    }

    private int findEmptyPositionCount(List<Elf> elves) {
        Set<Position> elfPositions = getAllElfPositions(elves);

        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minColumn = Integer.MAX_VALUE;
        int maxColumn = Integer.MIN_VALUE;

        for (Position position: elfPositions) {
            minRow = Math.min(minRow, position.row);
            maxRow = Math.max(maxRow, position.row);
            minColumn = Math.min(minColumn, position.column);
            maxColumn = Math.max(maxColumn, position.column);
        }

        int count = 0;
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                Position position = new Position(i, j);
                if (!elfPositions.contains(position)) {
                    count++;
                }
            }
        }
        return count;
    }

    private Set<Position> getAllElfPositions(List<Elf> elves) {
        return elves.stream().map(elf -> elf.currentPosition).collect(Collectors.toSet());
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day23input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
