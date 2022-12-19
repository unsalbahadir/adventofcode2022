package solutions.day19;

import java.util.*;
import java.util.function.Predicate;

public class Blueprint {

    int id;
    List<Robot> robots = new ArrayList<>();
    Map<ResourceType, Integer> resources;
    int minutesLeft;

    public Blueprint(int id) {
        this.id = id;
        resources = new HashMap<>();
        Arrays.stream(ResourceType.values()).forEach(value -> resources.put(value, 0));
    }

    public boolean hasResourceForRobot(Robot robot) {
        Map<ResourceType, Integer> cost = robot.getCost();
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            Integer currentResourceAmount = resources.get(entry.getKey());
            if (currentResourceAmount < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void buildRobot(Robot robot) {
        Map<ResourceType, Integer> cost = robot.getCost();
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            Integer currentResourceAmount = resources.get(entry.getKey());
            resources.put(entry.getKey(), currentResourceAmount - entry.getValue());
        }
        robots.add(robot);
    }

    public void collect() {
        robots.forEach(robot -> resources.merge(robot.collect(), 1, Integer::sum));

        minutesLeft--;
    }

    public Blueprint copy() {
        Blueprint copy = new Blueprint(this.id);
        copy.robots = new ArrayList<>();
        for (Robot robot : this.robots) {
            copy.robots.add(robot.copy());
        }
        copy.resources = new HashMap<>(this.resources);
        copy.minutesLeft = this.minutesLeft;

        return copy;
    }

    @Override
    public String toString() {
        return "Blueprint{" +
                "id=" + id +
                ", robotCounts=" + getRobotCounts() +
                ", resources=" + resources +
                '}';
    }

    private Map<ResourceType, Long> getRobotCounts() {
        Map<ResourceType, Long> robotCounts = new HashMap<>();
        for (ResourceType value : ResourceType.values()) {
            long robotCount = getRobotCount(value);
            robotCounts.put(value, robotCount);
        }
        return robotCounts;
    }

    public long getRobotCount(ResourceType resourceType) {
        Predicate<Robot> robotPredicate = robot -> {
            if (resourceType == ResourceType.ORE) {
                return robot instanceof OreRobot;
            } else if (resourceType == ResourceType.CLAY) {
                return robot instanceof ClayRobot;
            } else if (resourceType == ResourceType.OBSIDIAN) {
                return robot instanceof ObsidianRobot;
            } else {
                return robot instanceof GeodeRobot;
            }
        };
        return robots.stream()
                .filter(robotPredicate)
                .count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blueprint blueprint = (Blueprint) o;
        return minutesLeft == blueprint.minutesLeft && getRobotCounts().equals(blueprint.getRobotCounts()) && resources.equals(blueprint.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRobotCounts(), resources, minutesLeft);
    }
}
