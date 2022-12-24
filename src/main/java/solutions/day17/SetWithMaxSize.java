package solutions.day17;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SetWithMaxSize {

    int maxSize;
    private final TreeSet<Position> elements;

    public SetWithMaxSize(int maxSize) {
        this.maxSize = maxSize;
        elements = new TreeSet<>(getComparator());
    }

    private Comparator<Position> getComparator() {
        Comparator<Position> comparator = Comparator.comparing(position -> position.row);
        comparator = comparator.thenComparing(position -> position.column);
        return comparator;
    }

    public Set<Position> getElements() {
        return elements;
    }

    public Position getLast() {
        return elements.last();
    }

    public void add(Position blueprint) {
        elements.add(blueprint);
        if (elements.size() > maxSize) {
            elements.pollFirst();
        }
    }

    public int size() {
        return elements.size();
    }

    public void addAll(Collection<Position> collection) {
        collection.forEach(this::add);
    }
}
