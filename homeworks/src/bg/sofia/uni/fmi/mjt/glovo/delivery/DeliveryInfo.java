package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record DeliveryInfo(Location deliveryGuyLocation, double price, int estimatedTime, DeliveryType deliveryType) {

    public DeliveryInfo {
        if (estimatedTime < 0) {
            throw new IllegalArgumentException("Estimated time must be positive");
        }

        if (price < 0.0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}
