package solutions.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Solution {

    public int getMaxTotalCalories(List<String> lines) {
        int maxTotalCalories = Integer.MIN_VALUE;

        int currentTotal = 0;
        for (String line : lines) {
            if (line.equals("")) {
                maxTotalCalories = Math.max(maxTotalCalories, currentTotal);
                currentTotal = 0;
                continue;
            }
            currentTotal += Integer.parseInt(line);
        }

        return maxTotalCalories;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day1input.txt"));
        System.out.println(solution.getMaxTotalCalories(lines));
    }
}
