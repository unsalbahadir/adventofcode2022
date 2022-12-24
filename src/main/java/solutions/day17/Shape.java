package solutions.day17;

import java.util.ArrayList;
import java.util.List;

public class Shape {

    List<Position> positions = new ArrayList<>();

    public Shape() {
    }

    public Shape(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "positions=" + positions +
                '}';
    }
}
