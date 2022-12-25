package solutions.day25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Solution {

    public String getSolution(List<String> lines) {
        long sum = 0;
        for (String line : lines) {
            long snafuNumber = convertFromSnafu(line);
            sum += snafuNumber;
        }
        System.out.println(sum);
//        IntStream.of(10, 15, 20, 2022, 12345, 314159265).forEach(value -> {
//            System.out.println(value + ": " + convertToSnafu(value));
//        });

        return convertToSnafu(sum);
    }

    private long convertFromSnafu(String line) {
        long result = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            int number = convertDigitFromSnafu(c);
            int power = line.length() - 1 - i;
            double currentResult = Math.pow(5, power) * number;
            result += currentResult;
        }

        return result;
    }

    private static int convertDigitFromSnafu(char c) {
        int number;
        if (c == '-') {
            number = -1;
        } else if (c == '=') {
            number = -2;
        } else {
            number = Character.getNumericValue(c);
        }
        return number;
    }

    private String convertToSnafu(long number) {
        String numberInBaseFive = Long.toString(number, 5);
//        System.out.println(numberInBaseFive);

        StringBuilder result = new StringBuilder();
        char carry = '0';
        for (int i = numberInBaseFive.length() - 1; i >= 0; i--) {
            char c = numberInBaseFive.charAt(i);
            String snafuDigit = convertDigitToSnafu(Character.getNumericValue(c));
            if (snafuDigit.length() == 2) {
                String sumWithCarry = sumDigits(carry, snafuDigit.charAt(1));
                result.insert(0, sumWithCarry);
                carry = snafuDigit.charAt(0);
            } else {
                String sumWithCarry = sumDigits(carry, snafuDigit.charAt(0));
                if (sumWithCarry.length() == 2) {
                    result.insert(0, sumWithCarry.charAt(1));
                    carry = sumWithCarry.charAt(0);
                } else {
                    result.insert(0, sumWithCarry);
                    carry = '0';
                }
            }
        }
        if (carry != '0') {
            result.insert(0, carry);
        }
        return result.toString();
    }

    private String convertDigitToSnafu(int digit) {
        if (digit == 3) {
            return "1=";
        } else if (digit == 4) {
            return "1-";
        } else if (digit == -1) {
            return "-";
        } else if (digit == -2) {
            return "=";
        } else {
            return String.valueOf(digit);
        }
    }

    private String sumDigits(char firstDigit, char secondDigit) {
        int sum = convertDigitFromSnafu(firstDigit) + convertDigitFromSnafu(secondDigit);
        return convertDigitToSnafu(sum);
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day25input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
