package solutions.day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Solution {

    public int countFullyContainedPairs(List<String> lines) {
        int count = 0;

        for (String line : lines) {
            String[] pairs = line.split(",");
            if (containsOtherPair(pairs[0].split("-"), pairs[1].split("-"))) {
                count++;
            }
        }

        return count;
    }

    private boolean containsOtherPair(String[] firstPair, String[] secondPair) {
        int firstLow = Integer.parseInt(firstPair[0]);
        int firstHigh = Integer.parseInt(firstPair[1]);
        int secondLow = Integer.parseInt(secondPair[0]);
        int secondHigh = Integer.parseInt(secondPair[1]);

        if (firstLow >= secondLow && firstHigh <= secondHigh) {
            return true;
        } else if (firstLow <= secondLow && firstHigh >= secondHigh) {
            return true;
        }
        return false;
    }

    public int countOverlaps(List<String> lines) {
        int count = 0;

        for (String line : lines) {
            String[] pairs = line.split(",");
            if (overlaps(pairs[0].split("-"), pairs[1].split("-"))) {
                count++;
            }
        }

        return count;
    }

    private boolean overlaps(String[] firstPair, String[] secondPair) {
        int firstLow = Integer.parseInt(firstPair[0]);
        int firstHigh = Integer.parseInt(firstPair[1]);
        int secondLow = Integer.parseInt(secondPair[0]);
        int secondHigh = Integer.parseInt(secondPair[1]);

        if (firstLow >= secondLow && firstLow <= secondHigh) {
            return true;
        } else if (secondLow >= firstLow && secondLow <= firstHigh) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day4input.txt"));
        System.out.println(solution.countFullyContainedPairs(lines));
        System.out.println(solution.countOverlaps(lines));
    }
}
