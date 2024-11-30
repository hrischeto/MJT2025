package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public class MapLayout {
    private final MapEntity[][] layout;
    private final int rows;
    private final int cols;

    public MapLayout(char[][] mapLayout) {
        validateMap(mapLayout);

        rows = mapLayout.length;
        cols = mapLayout[0].length;

        layout = new MapEntity[rows][cols];
        setLayout(mapLayout);
    }

    private void setLayout(char[][] mapLayout) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                layout[i][j] = new MapEntity(new Location(i, j), MapEntityType.mapEntityTypeOf(mapLayout[i][j]));
            }
        }
    }

    private void validateMap(char[][] mapLayout) {
        if (mapLayout.length == 0) {
            throw new IllegalArgumentException("Empty map layout.");
        }

        int currentSize = mapLayout[0].length;
        for (int i = 1; i < mapLayout.length; i++) {
            if (mapLayout[i].length != currentSize) {
                throw new IllegalArgumentException("Map layout was not square.");
            }
        }
    }

    public MapEntity[][] getMapLayout() {
        return layout;
    }

    public MapEntity getMapEntityAtLocation(Location location) {
        if (!validateLocation(location)) {
            throw new IllegalArgumentException("Out of map location");
        }

        return layout[location.x()][location.y()];
    }

    public boolean validateLocation(Location location) {
        return location.x() < rows && location.y() < cols;
    }
}
