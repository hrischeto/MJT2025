package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.Objects;

public record Location(int x, int y) {
    public Location {
        if (x < 0) {
            throw new IllegalArgumentException("Rows must be positive");
        }

        if (y < 0) {
            throw new IllegalArgumentException("Columns must be positive");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
