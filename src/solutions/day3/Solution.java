package solutions.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {

    public int getPriorityTotal(List<String> lines) {
        int total = 0;

        for (String line : lines) {
            int half = line.length() / 2;
            String firstHalf = line.substring(0, half);
            String secondHalf = line.substring(half);

            Character commonCharacter = findCommonCharacter(firstHalf, secondHalf);
            total += getPriorityOfChar(commonCharacter);
        }

        return total;
    }

    private Character findCommonCharacter(String first, String second) {
        Set<Character> characters = new HashSet<>();
        for (char c : first.toCharArray()) {
            characters.add(c);
        }
        for (char c : second.toCharArray()) {
            if (characters.contains(c)) {
                return c;
            }
        }
        return null;
    }

    public int getIdentityBadges(List<String> lines) {
        int total = 0;

        List<String> group = new ArrayList<>();
        for (String line : lines) {
            group.add(line);
            if (group.size() == 3) {
                Character commonCharacterInGroup = getCommonCharacterInGroup(group);
                total += getPriorityOfChar(commonCharacterInGroup);
                group.clear();
            }
        }

        return total;
    }

    private Character getCommonCharacterInGroup(List<String> rucksacks) {
        Set<Character> characters = new HashSet<>();
        for (char c : rucksacks.get(0).toCharArray()) {
            characters.add(c);
        }
        for (int i = 1; i < rucksacks.size(); i++) {
            String rucksack = rucksacks.get(i);
            characters.removeIf(c -> rucksack.indexOf(c) == -1);
        }
        return characters.stream().findFirst().get();
    }

    private int getPriorityOfChar(Character character) {
        if (Character.isLowerCase(character)) {
            return ((int) character) - 96;
        } else {
            return ((int) character) - 38;
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day3input.txt"));
        System.out.println(solution.getPriorityTotal(lines));
        System.out.println(solution.getIdentityBadges(lines));
    }
}
