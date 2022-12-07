package solutions.day7;

import java.util.ArrayList;
import java.util.List;

public class Directory implements Node {

    String name;
    List<Node> children = new ArrayList<>();
    Directory parent;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    void addNode(Node node) {
        children.add(node);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return children.stream().mapToInt(Node::getSize).sum();
    }

    public Node findNode(String name) {
        return children.stream().filter(child -> child.getName().equals(name)).findFirst().orElse(null);
    }
}
