package solutions.day11;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

//    public int countLevelOfMonkeyBusiness(List<String> lines, int rounds, boolean divide) {
//        List<Monkey> monkeys = createMonkeys(lines);
//
//        for (int i = 0; i < rounds; i++) {
//            for (Monkey monkey : monkeys) {
//                for (Iterator<Long> iterator = monkey.items.iterator(); iterator.hasNext(); ) {
//                    Long item = iterator.next();
//                    item = monkey.operation.apply(item);
//                    if (divide) {
//                        item /= 3;
//                    }
//                    boolean isDivisible = (item % monkey.divisibleBy) == 0;
//
//                    Monkey monkeyToThrowTo;
//                    if (isDivisible) {
//                        monkeyToThrowTo = monkeys.get(monkey.monkeyToThrowToIfTrue);
//                    } else {
//                        monkeyToThrowTo = monkeys.get(monkey.monkeyToThrowToIfFalse);
//                    }
//                    monkeyToThrowTo.items.add(item);
//                    iterator.remove();
//                    monkey.numberOfInspections++;
//                }
//            }
//        }
//        return monkeys.stream()
//                .map(monkey -> monkey.numberOfInspections)
//                .sorted(Collections.reverseOrder())
//                .limit(2)
//                .reduce(1, (integer, integer2) -> integer * integer2);
//    }

    public Long countLevelOfMonkeyBusiness2(List<String> lines) {
        List<Monkey> monkeys = createMonkeys(lines);

        int dividedByTotalProduct = monkeys.stream()
                .map(monkey -> monkey.divisibleBy)
                .reduce(1, (integer, integer2) -> integer * integer2);
        for (int i = 0; i < 10000; i++) {
            for (Monkey monkey : monkeys) {
                for (Iterator<BigInteger> iterator = monkey.items.iterator(); iterator.hasNext(); ) {
                    BigInteger item = iterator.next();
                    item = monkey.operation.apply(item);
                    item = item.remainder(BigInteger.valueOf(dividedByTotalProduct));
                    boolean isDivisible = (item.remainder(BigInteger.valueOf(monkey.divisibleBy)).equals(BigInteger.ZERO));

                    Monkey monkeyToThrowTo;
                    if (isDivisible) {
                        monkeyToThrowTo = monkeys.get(monkey.monkeyToThrowToIfTrue);
                    } else {
                        monkeyToThrowTo = monkeys.get(monkey.monkeyToThrowToIfFalse);
                    }
                    monkeyToThrowTo.items.add(item);
                    iterator.remove();
                    monkey.numberOfInspections++;
                }
            }
        }
        return monkeys.stream()
                .map(monkey -> monkey.numberOfInspections)
                .sorted(Collections.reverseOrder())
                .limit(2)
                .reduce(1L, (integer, integer2) -> integer * integer2);
    }

    private List<Monkey> createMonkeys(List<String> lines) {
        List<Monkey> monkeys = new ArrayList<>();

        int i = 0;
        while (i < lines.size()) {
            String monkeyIndexLine = lines.get(i);
            String startingItemsLine = lines.get(i + 1);
            String operationLine = lines.get(i + 2);
            String divisibleByLine = lines.get(i + 3);
            String trueLine = lines.get(i + 4);
            String falseLine = lines.get(i + 5);

            Monkey monkey = new Monkey(getStartingItems(startingItemsLine),
                    getOperation(operationLine),
                    getDivisibleBy(divisibleByLine),
                    getMonkeyIfTrue(trueLine),
                    getMonkeyIfFalse(falseLine));
            monkeys.add(monkey);

            i += 7;
        }

        return monkeys;
    }

    private List<BigInteger> getStartingItems(String startingItemsLine) {
        String itemsPart = startingItemsLine.substring(startingItemsLine.lastIndexOf(":") + 2);
        String[] split = itemsPart.split(", ");

        return Arrays.stream(split).map(BigInteger::new).collect(Collectors.toList());
    }

    private Function<BigInteger, BigInteger> getOperation(String operationLine) {
        String operationPart = operationLine.substring(operationLine.lastIndexOf("=") + 2);
        String[] split = operationPart.split(" ");
        String operator = split[1];
        String argument = split[2];

        return (input -> {
            BigInteger number;
            if (argument.equals("old")) {
                number = input;
            } else {
                number = new BigInteger(argument);
            }
            if (operator.equals("*")) {
                return input.multiply(number);
            } else if (operator.equals("+")) {
                return input.add(number);
            } else if (operator.equals("/")) {
                return input.divide(number);
            } else {
                return input.subtract(number);
            }
        });

    }

    private int getDivisibleBy(String divisibleByLine) {
        String[] split = divisibleByLine.split(" ");
        return Integer.parseInt(split[split.length - 1]);
    }

    private int getMonkeyIfTrue(String trueLine) {
        String[] split = trueLine.split(" ");
        return Integer.parseInt(split[split.length - 1]);
    }

    private int getMonkeyIfFalse(String falseLine) {
        String[] split = falseLine.split(" ");
        return Integer.parseInt(split[split.length - 1]);
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day11input.txt"));
//        System.out.println(solution.countLevelOfMonkeyBusiness(lines, 20, true));
        System.out.println(solution.countLevelOfMonkeyBusiness2(lines));
    }
}
