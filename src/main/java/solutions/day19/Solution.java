package solutions.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public int getSolution(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            Blueprint blueprint = buildBlueprint(line);
            int numberOfGeodesOpened = runSimulation(blueprint, 24);
            System.out.println("Number of geodes for blueprint " + blueprint.id + ": " + numberOfGeodesOpened);
            int qualityLevel = blueprint.id * numberOfGeodesOpened;
            sum += qualityLevel;
        }

        return sum;
    }

    private Blueprint buildBlueprint(String line) {
        String[] split = line.split(" ");

        int id = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
        Blueprint blueprint = new Blueprint(id);

        OreRobot.cost = Map.of(ResourceType.ORE, Integer.parseInt(split[6]));
        ClayRobot.cost = Map.of(ResourceType.ORE, Integer.parseInt(split[12]));
        ObsidianRobot.cost = Map.of(
                ResourceType.ORE, Integer.parseInt(split[18]),
                ResourceType.CLAY, Integer.parseInt(split[21])
        );
        GeodeRobot.cost = Map.of(
                ResourceType.ORE, Integer.parseInt(split[27]),
                ResourceType.OBSIDIAN, Integer.parseInt(split[30])
        );

        OreRobot oreRobot = new OreRobot();
        blueprint.robots.add(oreRobot);
        return blueprint;
    }

    private int runSimulation(Blueprint starterBlueprint, int minutes) {
        Set<Blueprint> blueprints = new HashSet<>();
        blueprints.add(starterBlueprint);
        for (int i = 0; i < minutes; i++) {
            SetWithMaxSize nextPossibleBlueprints = new SetWithMaxSize(200000);
            for (Blueprint currentBlueprint : blueprints) {
                if (currentBlueprint.hasResourceForRobot(new GeodeRobot())) {
                    currentBlueprint.collect();
                    Blueprint copyWithGeode = currentBlueprint.copy();
                    copyWithGeode.buildRobot(new GeodeRobot());
                    nextPossibleBlueprints.add(copyWithGeode);
                } else {
                    List<Blueprint> possibleBlueprints = getPossibleBlueprints(currentBlueprint);
                    nextPossibleBlueprints.addAll(possibleBlueprints);
                }
//                if (nextPossibleBlueprints.size() % 1000 == 0) {
//                    System.out.println(nextPossibleBlueprints.size());
//                }
            }
            blueprints = nextPossibleBlueprints.getElements();
        }
        // leftover in the list after 24 minutes are finished
        Blueprint finalBlueprint = starterBlueprint;
        int maxNumberOfGeodes = 0;
        for (Blueprint blueprint : blueprints) {
            Integer geodeCount = blueprint.resources.get(ResourceType.GEODE);
            if (geodeCount > maxNumberOfGeodes) {
                maxNumberOfGeodes = geodeCount;
                finalBlueprint = blueprint;
            }
        }

        System.out.println(finalBlueprint);
        return maxNumberOfGeodes;
    }


    private List<Blueprint> getPossibleBlueprints(Blueprint blueprint) {
        List<Blueprint> blueprints = new ArrayList<>();

        boolean canBuildObsidian = blueprint.hasResourceForRobot(new ObsidianRobot());
        boolean canBuildClay = blueprint.hasResourceForRobot(new ClayRobot());
        boolean canBuildOre = blueprint.hasResourceForRobot(new OreRobot());

        blueprint.collect();
        blueprints.add(blueprint.copy()); // without building

        // build obsidian
        if (canBuildObsidian) {
            Blueprint copyWithObsidian = blueprint.copy();
            copyWithObsidian.buildRobot(new ObsidianRobot());
            blueprints.add(copyWithObsidian);
        }

        // build clay
        if (canBuildClay) {
            Blueprint copyWithClay = blueprint.copy();
            copyWithClay.buildRobot(new ClayRobot());
            blueprints.add(copyWithClay);
        }

        // build ore
        if (canBuildOre) {
            Blueprint copyWithOre = blueprint.copy();
            copyWithOre.buildRobot(new OreRobot());
            blueprints.add(copyWithOre);
        }

        return blueprints;
    }

    public int getSolution2(List<String> lines) {
        int result = 1;
        lines = lines.stream().limit(3).collect(Collectors.toList());
        for (String line : lines) {
            Blueprint blueprint = buildBlueprint(line);
            int numberOfGeodesOpened = runSimulation(blueprint, 32);
            System.out.println("Number of geodes for blueprint " + blueprint.id + ": " + numberOfGeodesOpened);
            result *= numberOfGeodesOpened;
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day19input.txt"));
//        System.out.println(solution.getSolution(lines));
        System.out.println(solution.getSolution2(lines));
    }
}
