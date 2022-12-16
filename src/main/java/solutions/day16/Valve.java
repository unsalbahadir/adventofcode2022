package solutions.day16;

import java.util.ArrayList;
import java.util.List;

public class Valve {

    String name;
    int flowRate;
    List<Valve> adjacentValves = new ArrayList<>();
    boolean isOpen;

    public Valve(String name, int flowRate) {
        this.name = name;
        this.flowRate = flowRate;
    }
}
