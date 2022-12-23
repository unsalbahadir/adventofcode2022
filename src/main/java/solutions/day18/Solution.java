package solutions.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    private class Position3D {
        int x;
        int y;
        int z;

        public Position3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Position3D(String  x, String y, String z) {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
            this.z = Integer.parseInt(z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position3D that = (Position3D) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "Position3D{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    public int getSolution(List<String> lines) {
        List<Position3D> positions = buildPositions(lines);

        return getSurfaceArea(positions);
    }

    private List<Position3D> buildPositions(List<String> lines) {
        List<Position3D> positions = new ArrayList<>();

        for (String line : lines) {
            String[] split = line.split(",");
            Position3D position = new Position3D(split[0], split[1], split[2]);
            positions.add(position);
        }
        return positions;
    }

    private int getSurfaceArea(List<Position3D> positions) {
        int surfaceArea = positions.size() * 6;
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                if (areConnected(positions.get(i), positions.get(j))) {
                    surfaceArea -= 2;
                }
            }
        }
        return surfaceArea;
    }

    private boolean areConnected(Position3D firstPosition, Position3D secondPosition) {
        int value = Math.abs(firstPosition.x - secondPosition.x) +
                Math.abs(firstPosition.y - secondPosition.y) +
                Math.abs(firstPosition.z - secondPosition.z);
        return value <= 1;
    }

    public int getSolution2(List<String> lines) {
        Set<Position3D> lavaPositions = new HashSet<>();

        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (String line : lines) {
            String[] split = line.split(",");
            Position3D position = new Position3D(split[0], split[1], split[2]);
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);

            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            lavaPositions.add(position);
        }

        int surfaceArea = getSurfaceArea(new ArrayList<>(lavaPositions));

        List<Position3D> possibleAirPockets = new ArrayList<>();
        Set<Position3D> notAirPockets = new HashSet<>();
        for (int x = minX - 1; x <= maxX + 1; x++) {
            for (int y = minY - 1; y <= maxY + 1; y++) {
                for (int z = minZ - 1; z <= maxZ + 1; z++) {
                    Position3D position = new Position3D(x, y, z);
                    if (isAirPocket(lavaPositions, position, minX, maxX, minY, maxY, minZ, maxZ)) {
                        possibleAirPockets.add(position);
                    } else if (!lavaPositions.contains(position)) {
                        notAirPockets.add(position);
                    }
                }
            }
        }

        // iterate a few times to remove all notAirPockets
        for (int i = 0; i < 5; i++) {
            Iterator<Position3D> iterator = possibleAirPockets.iterator();
            while (iterator.hasNext()) {
                Position3D possibleAirPocket = iterator.next();
                boolean connectedToOutside = isConnectedToOutside(possibleAirPocket, notAirPockets);
                if (connectedToOutside) {
                    iterator.remove();
                }
            }
        }

        surfaceArea -= getSurfaceArea(possibleAirPockets);

        return surfaceArea;
    }

    private boolean isConnectedToOutside(Position3D possibleAirPocket, Set<Position3D> notAirPockets) {
        for (Position3D notAirPocket : notAirPockets) {
            if (areConnected(possibleAirPocket, notAirPocket)) {
                notAirPockets.add(possibleAirPocket);
                return true;
            }
        }
        return false;
    }

    // needs to be surrounded with lava positions on all sides
    private boolean isAirPocket(Set<Position3D> lavaPositions, Position3D position, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        return !lavaPositions.contains(position)
                && goRight(lavaPositions, position, maxX)
                && goLeft(lavaPositions, position, minX)
                && goTop(lavaPositions, position, maxY)
                && goBottom(lavaPositions, position, minY)
                && goForward(lavaPositions, position, maxZ)
                && goBack(lavaPositions, position, minZ);

    }

    private boolean goRight(Set<Position3D> lavaPositions, Position3D position, int maxX) {
        for (int i = position.x + 1; i <= maxX + 1; i++) {
            Position3D currentPosition = new Position3D(i, position.y, position.z);
            if (lavaPositions.contains(currentPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean goLeft(Set<Position3D> lavaPositions, Position3D position, int minX) {
        for (int i = position.x - 1; i >= minX - 1; i--) {
            Position3D currentPosition = new Position3D(i, position.y, position.z);
            if (lavaPositions.contains(currentPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean goTop(Set<Position3D> lavaPositions, Position3D position, int maxY) {
        for (int i = position.y + 1; i <= maxY + 1; i++) {
            Position3D currentPosition = new Position3D(position.x, i, position.z);
            if (lavaPositions.contains(currentPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean goBottom(Set<Position3D> lavaPositions, Position3D position, int minY) {
        for (int i = position.y - 1; i >= minY - 1; i--) {
            Position3D currentPosition = new Position3D(position.x, i, position.z);
            if (lavaPositions.contains(currentPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean goForward(Set<Position3D> lavaPositions, Position3D position, int maxZ) {
        for (int i = position.z + 1; i <= maxZ + 1; i++) {
            Position3D currentPosition = new Position3D(position.x, position.y, i);
            if (lavaPositions.contains(currentPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean goBack(Set<Position3D> lavaPositions, Position3D position, int minZ) {
        for (int i = position.z - 1; i >= minZ - 1; i--) {
            Position3D currentPosition = new Position3D(position.x, position.y, i);
            if (lavaPositions.contains(currentPosition)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day18input.txt"));
        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
