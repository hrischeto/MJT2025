package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    final char typeSymbol;

    MapEntityType(char typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public boolean isDeliveryGuy() {
        return typeSymbol == 'A' || typeSymbol == 'B';
    }

    public boolean isObstacle() {
        return typeSymbol == '#';
    }

    public static MapEntityType mapEntityTypeOf(char symbol) {
        return
            switch (symbol) {
                case '.' -> ROAD;
                case '#' -> WALL;
                case 'R' -> RESTAURANT;
                case 'C' -> CLIENT;
                case 'A' -> DELIVERY_GUY_CAR;
                case 'B' -> DELIVERY_GUY_BIKE;
                default -> throw new IllegalArgumentException("Map does not contain such symbol.");
            };
    }
}