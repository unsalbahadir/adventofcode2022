package solutions.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Solution {

    public int findSumOfSignalStrengths(List<String> lines) {
        int sum = 0;
        int cycleToCheck = 20;
        int currentCycle = 1;
        int register = 1;

        for (String line : lines) {
            if (line.equals("noop")) {
                if (currentCycle == cycleToCheck) {
                    sum += register * cycleToCheck;
                    cycleToCheck += 40;
                }
                currentCycle++;
            } else {
                String[] s = line.split(" ");
                int addxAmount = Integer.parseInt(s[1]);
                if (currentCycle == cycleToCheck - 1 || currentCycle == cycleToCheck) {
                    sum += register * cycleToCheck;
                    cycleToCheck += 40;
                }
                register += addxAmount;
                currentCycle += 2;
            }

        }

        return sum;
    }

    public void renderCrtImage(List<String> lines) {
        char[][] grid = new char[6][40];

        int currentCycle = 1;
        int register = 1;

        for (String line : lines) {
            if (line.equals("noop")) {
                drawPixel(grid, currentCycle, register);
                currentCycle++;
            } else {
                String[] s = line.split(" ");
                int addxAmount = Integer.parseInt(s[1]);
                drawPixel(grid, currentCycle, register);
                drawPixel(grid, currentCycle + 1, register);
                register += addxAmount;
                currentCycle += 2;
            }
        }

        for (char[] ints : grid) {
            System.out.println(Arrays.toString(ints));
        }
    }

    private void drawPixel(char[][] grid, int cycle, int register) {
        int row = (cycle - 1) / grid[0].length;
        int column = (cycle - 1) % grid[0].length;
        if (column == register - 1 || column == register || column == register + 1) {
            grid[row][column] = '#';
        } else {
            grid[row][column] = '.';
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day10input.txt"));
//        System.out.println(solution.findSumOfSignalStrengths(lines));
        solution.renderCrtImage(lines);
    }
}
