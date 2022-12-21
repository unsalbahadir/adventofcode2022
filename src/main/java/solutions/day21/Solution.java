package solutions.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    public Long getSolution(List<String> lines) {
        Map<String, Monkey> monkeyMap = new HashMap<>();

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
            monkeyMap.put(name, monkey);
        }
        return getResultOfMonkey(monkeyMap, monkeyMap.get("root"));
    }

    private Long getResultOfMonkey(Map<String,Monkey> monkeyMap, Monkey monkey) {
        if (monkey.result != null) {
            return monkey.result;
        }
        Monkey firstMonkey = monkeyMap.get(monkey.firstMonkeyName);
        Monkey secondMonkey = monkeyMap.get(monkey.secondMonkeyName);

        Long firstNumber = firstMonkey.result;
        Long secondNumber = secondMonkey.result;

        if (firstNumber == null) {
            firstNumber = getResultOfMonkey(monkeyMap, firstMonkey);

        }
        if (secondNumber == null) {
            secondNumber = getResultOfMonkey(monkeyMap, secondMonkey);
        }
        monkey.result = monkey.doOperation(firstNumber, secondNumber);
        return monkey.result;
    }

    public Long getSolution2(List<String> lines) {
        Map<String, Monkey> monkeyMap = new HashMap<>();

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
            monkeyMap.put(name, monkey);
        }
        Monkey rootMonkey = monkeyMap.get("root");
        Monkey pathWithHuman;
        Monkey otherPath;
        Monkey firstMonkey = monkeyMap.get(rootMonkey.firstMonkeyName);
        Monkey secondMonkey = monkeyMap.get(rootMonkey.secondMonkeyName);
        if (pathLeadsToHuman(monkeyMap, firstMonkey)) {
            pathWithHuman = firstMonkey;
            otherPath = secondMonkey;
        } else {
            pathWithHuman = secondMonkey;
            otherPath = firstMonkey;
        }
        Long target = getResultOfMonkey(monkeyMap, otherPath);

        return findValueForTarget(monkeyMap, pathWithHuman, target);
    }

    private boolean pathLeadsToHuman(Map<String, Monkey> monkeyMap, Monkey monkey) {
        if (monkey == null) {
            return false;
        }
        if ("humn".equals(monkey.name) || "humn".equals(monkey.firstMonkeyName) || "humn".equals(monkey.secondMonkeyName)) {
            return true;
        }

        return pathLeadsToHuman(monkeyMap, monkeyMap.get(monkey.firstMonkeyName)) || pathLeadsToHuman(monkeyMap, monkeyMap.get(monkey.secondMonkeyName));
    }

    private Long findValueForTarget(Map<String, Monkey> monkeyMap, Monkey monkey, Long target) {
        Monkey pathWithHuman;
        Monkey otherPath;
        boolean isLeftSide;
        Monkey firstMonkey = monkeyMap.get(monkey.firstMonkeyName);
        Monkey secondMonkey = monkeyMap.get(monkey.secondMonkeyName);
        if (pathLeadsToHuman(monkeyMap, firstMonkey)) {
            pathWithHuman = firstMonkey;
            otherPath = secondMonkey;
            isLeftSide = true;
        } else {
            pathWithHuman = secondMonkey;
            otherPath = firstMonkey;
            isLeftSide = false;
        }
        Long otherPathResult = getResultOfMonkey(monkeyMap, otherPath);
        Long nextTarget = getNeededValueForTarget(target, otherPathResult, isLeftSide, monkey.operator);

        if (pathWithHuman.name.equals("humn")) {
            return nextTarget;
        }

        return findValueForTarget(monkeyMap, pathWithHuman, nextTarget);
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
//        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
