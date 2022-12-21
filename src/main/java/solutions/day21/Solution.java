package solutions.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    private final Map<String, Monkey> monkeys = new HashMap<>();

    public Long getSolution(List<String> lines) {
        buildMap(lines);
        return getResultOfMonkey(monkeys.get("root"));
    }

    private void buildMap(List<String> lines) {
        for (String line : lines) {
            String[] split = line.split(":");
            String name = split[0];
            Monkey monkey = new Monkey(name);

            String[] operation = split[1].trim().split(" ");
            if (operation.length > 1) {
                monkey.firstMonkeyName = operation[0];
                monkey.secondMonkeyName = operation[2];
                monkey.operator = operation[1];
            } else {
                monkey.result = Long.parseLong(operation[0]);
            }
            monkeys.put(name, monkey);
        }
    }

    private Long getResultOfMonkey(Monkey monkey) {
        if (monkey.result != null) {
            return monkey.result;
        }
        Monkey firstMonkey = monkeys.get(monkey.firstMonkeyName);
        Monkey secondMonkey = monkeys.get(monkey.secondMonkeyName);

        Long firstNumber = firstMonkey.result;
        Long secondNumber = secondMonkey.result;

        if (firstNumber == null) {
            firstNumber = getResultOfMonkey(firstMonkey);
        }
        if (secondNumber == null) {
            secondNumber = getResultOfMonkey(secondMonkey);
        }
        monkey.result = monkey.doOperation(firstNumber, secondNumber);
        return monkey.result;
    }

    public Long getSolution2(List<String> lines) {
        buildMap(lines);
        Monkey rootMonkey = monkeys.get("root");
        Monkey pathWithHuman;
        Monkey otherPath;
        Monkey firstMonkey = monkeys.get(rootMonkey.firstMonkeyName);
        Monkey secondMonkey = monkeys.get(rootMonkey.secondMonkeyName);
        if (pathLeadsToHuman(firstMonkey)) {
            pathWithHuman = firstMonkey;
            otherPath = secondMonkey;
        } else {
            pathWithHuman = secondMonkey;
            otherPath = firstMonkey;
        }
        Long target = getResultOfMonkey(otherPath);

        return findValueForTarget(pathWithHuman, target);
    }

    private boolean pathLeadsToHuman(Monkey monkey) {
        if (monkey == null) {
            return false;
        }
        if ("humn".equals(monkey.name) || "humn".equals(monkey.firstMonkeyName) || "humn".equals(monkey.secondMonkeyName)) {
            return true;
        }

        return pathLeadsToHuman(monkeys.get(monkey.firstMonkeyName)) || pathLeadsToHuman(monkeys.get(monkey.secondMonkeyName));
    }

    private Long findValueForTarget(Monkey monkey, Long target) {
        Monkey pathWithHuman;
        Monkey otherPath;
        boolean isLeftSide;
        Monkey firstMonkey = monkeys.get(monkey.firstMonkeyName);
        Monkey secondMonkey = monkeys.get(monkey.secondMonkeyName);
        if (pathLeadsToHuman(firstMonkey)) {
            pathWithHuman = firstMonkey;
            otherPath = secondMonkey;
            isLeftSide = true;
        } else {
            pathWithHuman = secondMonkey;
            otherPath = firstMonkey;
            isLeftSide = false;
        }
        Long otherPathResult = getResultOfMonkey(otherPath);
        Long nextTarget = getNeededValueForTarget(target, otherPathResult, isLeftSide, monkey.operator);

        if (pathWithHuman.name.equals("humn")) {
            return nextTarget;
        }

        return findValueForTarget(pathWithHuman, nextTarget);
    }

    private long getNeededValueForTarget(long target, long otherPathResult, boolean isLeftSide, String operator) {
        switch (operator) {
            case "+":
                return target - otherPathResult;
            case "-":
                if (isLeftSide) {
                    return target + otherPathResult;
                } else {
                    return otherPathResult - target;
                }
            case "*":
                return target / otherPathResult;
            default:
                if (isLeftSide) {
                    return target * otherPathResult;
                } else {
                    return otherPathResult / target;
                }
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day21input.txt"));
        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
