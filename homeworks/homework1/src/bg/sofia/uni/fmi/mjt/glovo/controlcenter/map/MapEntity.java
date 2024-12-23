package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.Objects;

public record MapEntity(Location location, MapEntityType mapEntityType) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapEntity mapEntity = (MapEntity) o;
        return Objects.equals(location, mapEntity.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }
}
