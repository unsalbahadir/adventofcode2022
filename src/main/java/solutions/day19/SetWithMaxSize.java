package solutions.day19;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SetWithMaxSize {

    int maxSize;
    private final TreeSet<Blueprint> elements;

    public SetWithMaxSize(int maxSize) {
        this.maxSize = maxSize;
        elements = new TreeSet<>(getComparator());
    }

    private Comparator<Blueprint> getComparator() {
        Comparator<Blueprint> comparing = Comparator.comparing(blueprint -> blueprint.resources.get(ResourceType.GEODE));
        comparing = comparing.thenComparing(blueprint -> blueprint.getRobotCount(ResourceType.GEODE));
        comparing = comparing.thenComparing(blueprint -> blueprint.resources.get(ResourceType.OBSIDIAN));
        comparing = comparing.thenComparing(blueprint -> blueprint.getRobotCount(ResourceType.OBSIDIAN));
        comparing = comparing.thenComparing(blueprint -> blueprint.resources.get(ResourceType.CLAY));
        comparing = comparing.thenComparing(blueprint -> blueprint.getRobotCount(ResourceType.CLAY));
        comparing = comparing.thenComparing(blueprint -> blueprint.resources.get(ResourceType.ORE));
        comparing = comparing.thenComparing(blueprint -> blueprint.getRobotCount(ResourceType.ORE));

        return comparing;
    }

    public Set<Blueprint> getElements() {
        return elements;
    }

    public void add(Blueprint blueprint) {
        boolean add = elements.add(blueprint);
        if (elements.size() > maxSize) {
            elements.pollFirst();
        }
    }

    public int size() {
        return elements.size();
    }

    public void addAll(Collection<Blueprint> collection) {
        collection.forEach(this::add);
    }
}
