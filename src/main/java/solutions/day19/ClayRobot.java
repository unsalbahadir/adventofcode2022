package solutions.day19;

import java.util.Map;

public class ClayRobot extends Robot {

    static Map<ResourceType, Integer> cost;

    @Override
    public Map<ResourceType, Integer> getCost() {
        return cost;
    }

    @Override
    public ResourceType collect() {
        return ResourceType.CLAY;
    }
}
