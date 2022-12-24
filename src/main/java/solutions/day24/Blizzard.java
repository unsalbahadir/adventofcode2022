package solutions.day24;

import java.util.Objects;

class Blizzard {
    Position position;
    Direction direction;

    public Blizzard(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Blizzard copy() {
        return new Blizzard(position.copy(), direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blizzard blizzard = (Blizzard) o;
        return Objects.equals(position, blizzard.position) && direction == blizzard.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, direction);
    }

    @Override
    public String toString() {
        return "Blizzard{" +
                "position=" + position +
                ", direction=" + direction +
                '}';
    }
}
