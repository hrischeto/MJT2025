package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

import java.util.Objects;

public record Delivery(Location client, Location restaurant, Location deliveryGuy, String foodItem, double price,
                       int estimatedTime) {

    public Delivery {

        if (Objects.isNull(client)) {
            throw new IllegalArgumentException("Client is null.");
        }

        if (Objects.isNull(restaurant)) {
            throw new IllegalArgumentException("Restaurant is null.");
        }

        if (Objects.isNull(deliveryGuy)) {
            throw new IllegalArgumentException("Delivery guy is null.");
        }

        if (Objects.isNull(foodItem)) {
            throw new IllegalArgumentException("Food item is null.");
        }

        if (estimatedTime < 0) {
            throw new IllegalArgumentException("Estimated time must be positive");
        }

        if (price < 0.0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}