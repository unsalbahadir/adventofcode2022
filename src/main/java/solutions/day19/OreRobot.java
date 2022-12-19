package solutions.day19;

import java.util.Map;

public class OreRobot extends Robot {

    static Map<ResourceType, Integer> cost;

    @Override
    public Map<ResourceType, Integer> getCost() {
        return cost;
    }

    @Override
    public ResourceType collect() {
        return ResourceType.ORE;
    }
}
