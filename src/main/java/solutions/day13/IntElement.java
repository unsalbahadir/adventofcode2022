package solutions.day13;

public class IntElement implements Element {

    int value;

    public IntElement(int value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
