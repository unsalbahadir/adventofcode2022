package solutions.day13;

import java.util.ArrayList;
import java.util.List;

public class ListElement implements Element {

    List<Element> elements = new ArrayList<>();

    @Override
    public Object getValue() {
        return elements;
    }
}
