package solutions.day21;

public class Monkey {

    String name;
    Long result;

    String firstMonkeyName;
    String secondMonkeyName;
    String operator;

    public Monkey(String name) {
        this.name = name;
    }

    public long doOperation(long firstNumber, long secondNumber) {
        return switch (operator) {
            case "+" -> firstNumber + secondNumber;
            case "-" -> firstNumber - secondNumber;
            case "*" -> firstNumber * secondNumber;
            default -> firstNumber / secondNumber;
        };
    }
}
