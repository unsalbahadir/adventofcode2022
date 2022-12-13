package solutions.day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {

    public int getMarkerIndex(List<String> lines) {
        if (lines.size() > 1) {
            return -1;
        }

        List<Character> characters = new ArrayList<>();
        String line = lines.get(0);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            characters.add(c);
            if (characters.size() == 4) {
                if (areAllUnique(characters)) {
                    return i + 1;
                } else {
                    characters.remove(0);
                }
            }
        }
        return -1;
    }

    public int getStartMarkerIndex(List<String> lines) {
        if (lines.size() > 1) {
            return -1;
        }

        List<Character> characters = new ArrayList<>();
        String line = lines.get(0);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            characters.add(c);
            if (characters.size() == 14) {
                if (areAllUnique(characters)) {
                    return i + 1;
                } else {
                    characters.remove(0);
                }
            }
        }
        return -1;
    }

    private boolean areAllUnique(List<Character> characters) {
        Set<Character> characterSet = new HashSet<>(characters);
        return characterSet.size() == characters.size();
    }



    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day6input.txt"));
        System.out.println(solution.getMarkerIndex(lines));
        System.out.println(solution.getStartMarkerIndex(lines));
    }
}
