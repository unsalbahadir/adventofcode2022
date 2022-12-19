package solutions.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private class Position3D {
        int x;
        int y;
        int z;

        public Position3D(String  x, String y, String z) {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
            this.z = Integer.parseInt(z);
        }
    }

    public int getSolution(List<String> lines) {
        List<Position3D> positions = new ArrayList<>();

        for (String line : lines) {
            String[] split = line.split(",");
            Position3D position = new Position3D(split[0], split[1], split[2]);
            positions.add(position);
        }

        int surfaceArea = lines.size() * 6;
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

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day18input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
