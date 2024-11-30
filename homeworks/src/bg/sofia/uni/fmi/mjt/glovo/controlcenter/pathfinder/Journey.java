package bg.sofia.uni.fmi.mjt.glovo.controlcenter.pathfinder;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;

import java.util.Objects;

public class Journey {

    private int pathLengthRestaurantClient = 0;
    private MapEntity optimalDeliveryGuy = null;
    private int estimatedTimeDeliveryGuyRestaurant = 0;
    private double priceDeliveryGuyRestaurant = 0.0;

    public void setPathLengthRestaurantClient(int pathLength) {
        pathLengthRestaurantClient = pathLength;
    }

    public Location getDeliveryGuy() {
        if (Objects.isNull(optimalDeliveryGuy)) {
            return null;
        }

        return optimalDeliveryGuy.location();
    }

    public void setDeliveryGuy(MapEntity optimalDeliveryGuy) {
        this.optimalDeliveryGuy = optimalDeliveryGuy;
    }

    public int getPathLengthRestaurantClient() {
        return pathLengthRestaurantClient;
    }

    public void resetDeliveryJourney() {

        setDeliveryGuy(null);
        setPathLengthRestaurantClient(0);
        setEstimatedTimeDeliveryGuyRestaurant(0);
        setPriceDeliveryGuyRestaurant(0.0);
    }

    public void setPriceDeliveryGuyRestaurant(double priceDeliveryGuyRestaurant) {
        this.priceDeliveryGuyRestaurant = priceDeliveryGuyRestaurant;
    }

    public void setEstimatedTimeDeliveryGuyRestaurant(int estimatedTimeDeliveryGuyRestaurant) {
        this.estimatedTimeDeliveryGuyRestaurant = estimatedTimeDeliveryGuyRestaurant;
    }

    public double getPriceDeliveryGuyRestaurant() {
        return priceDeliveryGuyRestaurant;
    }

    public int getEstimatedTimeDeliveryGuyRestaurant() {
        return estimatedTimeDeliveryGuyRestaurant;
    }
}
