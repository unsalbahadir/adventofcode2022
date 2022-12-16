package solutions.day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    private final Map<String, Valve> valves = new HashMap<>();

    public int getSolution(List<String> lines) {
        buildValves(lines);

        Valve startingValve = valves.get("AA");
        return traverse(startingValve, null, 30, 0);
    }

    private void buildValves(List<String> lines) {
        parseValves(lines);
        linkValves(lines);
    }

    private void parseValves(List<String> lines) {
        for (String line : lines) {
            Valve valve = parseValve(line);
            valves.put(valve.name, valve);
        }
    }

    private Valve parseValve(String line) {
        String[] split = line.split(";");
        String firstPart = split[0];
        String[] s = firstPart.split(" ");
        return new Valve(s[1], getFlowRate(s[4]));
    }

    private int getFlowRate(String s) {
        int start = s.indexOf("=");

        return Integer.parseInt(s.substring(start + 1));
    }

    private void linkValves(List<String> lines) {
        for (String line : lines) {
            String[] split = line.split(";");
            String secondPart = split[1].trim();
            String[] s = secondPart.split(" ");
            for (int i = 4; i < s.length; i++) {
                String linkedValveName = s[i];
                linkedValveName = linkedValveName.replace(",", "");
                Valve valve = parseValve(line);
                valves.get(valve.name).adjacentValves.add(valves.get(linkedValveName));
            }
        }
    }

    private int traverse(Valve currentValve, Valve previousValve, int minutesLeft, int totalPressureReleased) {
        if (minutesLeft <= 0) {
            return totalPressureReleased;
        }

        totalPressureReleased += getCurrentFlowRate();
        int maxTotalPressureReleased = totalPressureReleased;

        if (areAllValvesOpen()) { // if all are open, no need to move again
            return maxTotalPressureReleased;
        }
        // move or open
        for (Valve adjacentValve : currentValve.adjacentValves) {
            if (currentValve.adjacentValves.size() > 1 && adjacentValve == previousValve) {
                continue;
            }
            int pressureReleasedWithMove = traverse(adjacentValve, currentValve, minutesLeft - 1, totalPressureReleased);
            int currentMax = pressureReleasedWithMove;
            if (!currentValve.isOpen && currentValve.flowRate != 0) {
                currentValve.isOpen = true;
                boolean addCurrentFlowRate = minutesLeft > 1;
                int pressureDuringSecondMinute = addCurrentFlowRate ? getCurrentFlowRate() : 0;
                int pressureReleasedWithOpen = traverse(adjacentValve, currentValve, minutesLeft - 2, totalPressureReleased + pressureDuringSecondMinute);
                currentValve.isOpen = false;
                if (pressureReleasedWithOpen >= pressureReleasedWithMove) {
                    currentMax = pressureReleasedWithOpen;
                }
            }
            maxTotalPressureReleased = Math.max(maxTotalPressureReleased, currentMax);
        }

        return maxTotalPressureReleased;
    }

    private int getCurrentFlowRate() {
        return valves.values().stream()
                .filter(valve -> valve.isOpen)
                .mapToInt(valve -> valve.flowRate)
                .sum();
    }

    private boolean areAllValvesOpen() {
        return valves.values().stream()
                .filter(valve -> valve.flowRate != 0)
                .allMatch(valve -> valve.isOpen);
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day16input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
