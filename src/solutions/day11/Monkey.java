package solutions.day11;

import java.util.List;
import java.util.function.Function;

public class Monkey {

    List<Long> items;
    Function<Long, Long> operation;
    int divisibleBy;
    int monkeyToThrowToIfTrue;
    int monkeyToThrowToIfFalse;

    int numberOfInspections;
    public Monkey(List<Long> items, Function<Long, Long> operation, int divisibleBy, int monkeyToThrowToIfTrue, int monkeyToThrowToIfFalse) {
        this.items = items;
        this.operation = operation;
        this.divisibleBy = divisibleBy;
        this.monkeyToThrowToIfTrue = monkeyToThrowToIfTrue;
        this.monkeyToThrowToIfFalse = monkeyToThrowToIfFalse;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "items=" + items +
                ", operation=" + operation +
                ", divisibleBy=" + divisibleBy +
                ", monkeyToThrowToIfTrue=" + monkeyToThrowToIfTrue +
                ", monkeyToThrowToIfFalse=" + monkeyToThrowToIfFalse +
                ", numberOfInspections=" + numberOfInspections +
                '}';
    }
}
