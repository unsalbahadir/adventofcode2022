package solutions.day19;

import java.util.Map;

public abstract class Robot {

    Robot copy() {
        return getRobotForResource(collect());
    }
    abstract Map<ResourceType, Integer> getCost();
    abstract ResourceType collect();

    public static Robot getRobotForResource(ResourceType resourceType) {
        if (resourceType == ResourceType.ORE) {
            return new OreRobot();
        } else if (resourceType == ResourceType.CLAY) {
            return new ClayRobot();
        } else if (resourceType == ResourceType.OBSIDIAN) {
            return new ObsidianRobot();
        } else {
            return new GeodeRobot();
        }
    }
}
