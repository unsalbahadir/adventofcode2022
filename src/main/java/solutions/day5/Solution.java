package solutions.day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {

    List<Stack<String>> stacks = new ArrayList<>();

    public String getTopOfStacks(List<String> lines) {
        List<String> setUpLines = new ArrayList<>();

        int i = 0;
        for (; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("move")) {
                break;
            }
            setUpLines.add(line);
        }
        buildStacks(setUpLines);

        List<String> moveLines = new ArrayList<>();
        for (; i < lines.size(); i++) {
            String line = lines.get(i);
            moveLines.add(line);
        }
        handleMoves(moveLines);

        return stacks.stream().map(Stack::peek).collect(Collectors.joining());
    }

    public String getTopOfStacks2(List<String> lines) {
        List<String> setUpLines = new ArrayList<>();

        int i = 0;
        for (; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("move")) {
                break;
            }
            setUpLines.add(line);
        }
        buildStacks(setUpLines);

        List<String> moveLines = new ArrayList<>();
        for (; i < lines.size(); i++) {
            String line = lines.get(i);
            moveLines.add(line);
        }
        handleMoves2(moveLines);

        return stacks.stream().map(Stack::peek).collect(Collectors.joining());
    }

    private void buildStacks(List<String> lines) {
        stacks.clear();

        String numberLine = lines.get(lines.size() - 2);
        String[] numbers = numberLine.split("   ");
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = numbers[i].trim();
        }
        int sizeOfStack = Integer.parseInt(numbers[numbers.length - 1]);
        IntStream.range(0, sizeOfStack).forEach(i -> stacks.add(new Stack<>()));

        for (int i = lines.size() - 3; i >= 0; i--) {
            String line = lines.get(i);
            for (String stackNumber : numbers) {
                int indexOfNumber = numberLine.indexOf(stackNumber);
                if (line.length() > indexOfNumber) {
                    char c = line.charAt(indexOfNumber);
                    if (Character.isLetter(c)) {
                        stacks.get(Integer.parseInt(stackNumber) - 1).add(String.valueOf(c));
                    }
                }
            }
        }

    }

    private void handleMoves(List<String> lines) {
        for (String line : lines) {
            String[] words = line.split(" ");
            int amount = Integer.parseInt(words[1]);
            int from = Integer.parseInt(words[3]);
            int to = Integer.parseInt(words[5]);

            for (int i = 0; i < amount; i++) {
                String crateToMove = stacks.get(from - 1).pop();
                stacks.get(to - 1).add(crateToMove);
            }
        }
    }

    private void handleMoves2(List<String> lines) {
        for (String line : lines) {
            String[] words = line.split(" ");
            int amount = Integer.parseInt(words[1]);
            int from = Integer.parseInt(words[3]);
            int to = Integer.parseInt(words[5]);

            List<String> cratesToMove = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                String crateToMove = stacks.get(from - 1).pop();
                cratesToMove.add(crateToMove);
            }
            for (int i = cratesToMove.size() - 1; i >= 0; i--) {
                String crateToMove = cratesToMove.get(i);
                stacks.get(to - 1).add(crateToMove);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day5input.txt"));
        System.out.println(solution.getTopOfStacks(lines));
        System.out.println(solution.getTopOfStacks2(lines));
    }
}
