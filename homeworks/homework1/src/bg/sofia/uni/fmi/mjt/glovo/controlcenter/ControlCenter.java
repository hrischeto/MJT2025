package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapLayout;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.pathfinder.PathFinder;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;

import java.util.Objects;

public class ControlCenter implements ControlCenterApi {
    final int NO_TIME_CONSTRAINT = -1;
    final double NO_PRICE_CONSTRAINT = -1.0;

    private final MapLayout map;
    private final PathFinder pathFinder;

    public ControlCenter(char[][] mapLayout) {
        map = new MapLayout(mapLayout);
        pathFinder = new PathFinder(map, NO_TIME_CONSTRAINT, NO_PRICE_CONSTRAINT);
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation,
                                               double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        validateArguments(restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);

        return pathFinder.getDeliveryInfo(restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);
    }

    @Override
    public MapEntity[][] getLayout() {
        return map.getMapLayout();
    }

    private void validateArguments(Location restaurantLocation, Location clientLocation,
                                   double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        validateClient(clientLocation);
        validateRestaurant(restaurantLocation);

        validatePriceConstraint(maxPrice);
        validateTimeConstraint(maxTime);

        validateShippingMethod(shippingMethod);
    }

    private void validateLocation(Location location) {
        if (Objects.isNull(location)) {
            throw new IllegalArgumentException("Location was null.");
        }
    }

    private void validateClient(Location clientLocation) {

        try {
            validateLocation(clientLocation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Client location was null.");
        }

        if (!map.validateLocation(clientLocation)) {

            throw new InvalidOrderException("Client location is out of the map.");
        }

        if (!map.getMapEntityAtLocation(clientLocation).mapEntityType().equals(MapEntityType.CLIENT)) {

            throw new InvalidOrderException("Map entity on location is not a client.");
        }
    }

    private void validateRestaurant(Location restaurantLocation) {

        try {
            validateLocation(restaurantLocation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Restaurant location was null.");
        }

        if (!map.validateLocation(restaurantLocation)) {

            throw new InvalidOrderException("Restaurant location is out of the map.");
        }

        if (!map.getMapEntityAtLocation(restaurantLocation).mapEntityType().equals(MapEntityType.RESTAURANT)) {

            throw new InvalidOrderException("Map entity on location is not a restaurant.");
        }
    }

    private void validateShippingMethod(ShippingMethod shippingMethod) {
        if (Objects.isNull(shippingMethod)) {
            throw new IllegalArgumentException("Shipping method was null.");
        }
    }

    private void validateTimeConstraint(int time) {
        if (!(time == NO_TIME_CONSTRAINT || time > 0)) {
            throw new IllegalArgumentException("Invalid time constraint.");
        }
    }

    private void validatePriceConstraint(double price) {
        if (!(Double.compare(price, NO_PRICE_CONSTRAINT) == 0 || Double.compare(price, 0) > 0)) {
            throw new IllegalArgumentException("Invalid price constraint.");
        }
    }
}
