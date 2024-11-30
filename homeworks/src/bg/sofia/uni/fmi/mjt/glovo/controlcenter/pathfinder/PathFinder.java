package bg.sofia.uni.fmi.mjt.glovo.controlcenter.pathfinder;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapLayout;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.UnreachableClientException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class PathFinder {
    protected final MapLayout map;
    protected final int noTimeConstraintsValue;
    protected final double noPriceConstraintsValue;
    protected final Journey deliveryJourney;

    private final int[][] moveVector = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public PathFinder(MapLayout map, int noTimeConstraintsValue, double noPriceConstraintsValue) {

        this.map = map;
        this.noTimeConstraintsValue = noTimeConstraintsValue;
        this.noPriceConstraintsValue = noPriceConstraintsValue;
        deliveryJourney = new Journey();
    }

    public DeliveryInfo getDeliveryInfo(Location restaurantLocation, Location clientLocation,
                                        double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        getDeliveryJourneyInfo(restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);

        double deliveryPrice = getDeliveryPrice();
        int estimatedDeliveryTime = getEstimatedDeliveryTime();
        DeliveryType deliveryType = getDeliveryType();

        return new DeliveryInfo(deliveryJourney.getDeliveryGuy(), deliveryPrice, estimatedDeliveryTime, deliveryType);
    }

    private void getDeliveryJourneyInfo(Location restaurantLocation, Location clientLocation,
                                        double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        deliveryJourney.resetDeliveryJourney();

        deliveryJourney.setPathLengthRestaurantClient(
            getLengthOfShortestPath(map.getMapEntityAtLocation(restaurantLocation),
                map.getMapEntityAtLocation(clientLocation)));

        if (deliveryJourney.getPathLengthRestaurantClient() == -1) {
            throw new UnreachableClientException("No existing path between restaurant and client.");
        }

        getPathBetween(restaurantLocation, MapEntityType.DELIVERY_GUY_BIKE, maxPrice, maxTime, shippingMethod);
        getPathBetween(restaurantLocation, MapEntityType.DELIVERY_GUY_CAR, maxPrice, maxTime, shippingMethod);
    }

    private void fillNextLevel(Set<Location> visited, Queue<Location> queue, Location current) {

        int numberOfDirection = 4;
        List<Location> directions = new ArrayList<>(numberOfDirection);

        for (int i = 0; i < numberOfDirection; i++) {
            if (current.x() + moveVector[i][0] >= 0 && current.y() + moveVector[i][1] >= 0) {
                directions.add(new Location(current.x() + moveVector[i][0], current.y() + moveVector[i][1]));
            }
        }

        for (Location location : directions) {

            if (map.validateLocation(location) &&
                !map.getMapEntityAtLocation(location).mapEntityType().isObstacle() && !visited.contains(location)) {

                queue.add(location);
            }
        }
    }

    private double getDeliveryPrice() {
        DeliveryType deliveryType = getDeliveryType();

        return deliveryType.getPricePerKilometer() * deliveryJourney.getPathLengthRestaurantClient()
            + deliveryJourney.getPriceDeliveryGuyRestaurant();
    }

    private DeliveryType getDeliveryType() {

        if ((map.getMapEntityAtLocation(deliveryJourney.getDeliveryGuy()).mapEntityType()).equals(
            MapEntityType.DELIVERY_GUY_BIKE)) {
            return DeliveryType.BIKE;

        } else if ((map.getMapEntityAtLocation(deliveryJourney.getDeliveryGuy()).mapEntityType()).equals(
            MapEntityType.DELIVERY_GUY_CAR)) {
            return DeliveryType.CAR;
        } else {
            throw new InvalidMapEntityException("No existing delivery type for given delivery guy.");
        }
    }

    private int getEstimatedDeliveryTime() {
        DeliveryType deliveryType = getDeliveryType();

        return deliveryType.getTimePerKilometer() * deliveryJourney.getPathLengthRestaurantClient()
            + deliveryJourney.getEstimatedTimeDeliveryGuyRestaurant();
    }

    private int getLengthOfShortestPath(MapEntity entity1, MapEntity entity2) {

        int pathLength = 0;
        Queue<Location> queue = new ArrayDeque<>();
        Set<Location> visited = new HashSet<>();

        queue.add(entity1.location());

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            while (levelSize > 0) {
                Location current = queue.remove();
                if (current.equals(entity2.location())) {
                    return pathLength;
                }

                visited.add(current);
                fillNextLevel(visited, queue, current);

                levelSize--;
            }
            pathLength++;
        }
        return -1;
    }

    private void getPathBetween(Location from, MapEntityType toFirst, double maxPrice, int maxTime,
                                ShippingMethod shippingMethod) {
        Queue<Location> queue = new ArrayDeque<>();
        Set<Location> visited = new HashSet<>();

        queue.add(from);

        int pathLength = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            while (levelSize > 0) {
                Location current = queue.remove();

                if (map.getMapEntityAtLocation(current).mapEntityType().equals(toFirst)) {

                    if (setMostOptimal(toFirst, pathLength, maxPrice, maxTime, shippingMethod)) {

                        deliveryJourney.setDeliveryGuy(map.getMapEntityAtLocation(current));
                        return;
                    }
                }

                visited.add(current);
                fillNextLevel(visited, queue, current);

                levelSize--;
            }
            pathLength++;
        }
    }

    private int getTime(MapEntityType type, int pathLength) {
        int time;

        if (type == MapEntityType.DELIVERY_GUY_BIKE) {
            time = DeliveryType.BIKE.getTimePerKilometer() * pathLength;

        } else if (type == MapEntityType.DELIVERY_GUY_CAR) {
            time = DeliveryType.CAR.getTimePerKilometer() * pathLength;

        } else {
            throw new InvalidMapEntityException("No existing delivery type for given delivery guy.");
        }
        return time;
    }

    private double getPrice(MapEntityType type, int pathLength) {
        int price;

        if (type == MapEntityType.DELIVERY_GUY_BIKE) {
            price = DeliveryType.BIKE.getPricePerKilometer() * pathLength;

        } else if (type == MapEntityType.DELIVERY_GUY_CAR) {
            price = DeliveryType.CAR.getPricePerKilometer() * pathLength;

        } else {
            throw new InvalidMapEntityException("No existing delivery type for given delivery guy.");
        }
        return price;
    }

    private boolean setMostOptimal(MapEntityType type, int pathLength, double maxPrice, int maxTime,
                                   ShippingMethod shippingMethod) {

        int timeRestaurantClient = getTime(type, deliveryJourney.getPathLengthRestaurantClient());
        double priceRestaurantClient = getPrice(type, deliveryJourney.getPathLengthRestaurantClient());

        int totalTime = getTime(type, pathLength) + timeRestaurantClient;
        double totalPrice = getPrice(type, pathLength) + priceRestaurantClient;

        boolean isTimeInLimits = maxTime == noTimeConstraintsValue || totalTime < maxTime;
        boolean isPriceInLimits = maxPrice == noPriceConstraintsValue ||
            Double.compare(totalPrice, maxPrice) <= 0;

        //if we have not yet assigned anything to deliveryJourney
        if (deliveryJourney.getPriceDeliveryGuyRestaurant() == 0.0 &&
            deliveryJourney.getEstimatedTimeDeliveryGuyRestaurant() == 0) {

            if (!isPriceInLimits || !isTimeInLimits) {
                return false;
            }
        } else {

            if (!isTimeInLimits || !isPriceInLimits || !isMoreOptimal(totalPrice, totalTime, shippingMethod)) {
                return false;
            }
        }

        deliveryJourney.setEstimatedTimeDeliveryGuyRestaurant(totalTime - timeRestaurantClient);
        deliveryJourney.setPriceDeliveryGuyRestaurant(totalPrice - priceRestaurantClient);
        return true;
    }

    private boolean isMoreOptimal(double price, int time, ShippingMethod shippingMethod) {
        if (Objects.isNull(shippingMethod)) {
            throw new IllegalArgumentException("Shipping method is null");
        }

        boolean isMoreOptimal = false;

        if (shippingMethod == ShippingMethod.CHEAPEST) {
            isMoreOptimal = price < deliveryJourney.getPriceDeliveryGuyRestaurant() +
                getPrice(map.getMapEntityAtLocation(deliveryJourney.getDeliveryGuy()).mapEntityType(),
                    deliveryJourney.getPathLengthRestaurantClient());

        } else if (shippingMethod == ShippingMethod.FASTEST) {
            isMoreOptimal = time < deliveryJourney.getEstimatedTimeDeliveryGuyRestaurant() +
                getTime(map.getMapEntityAtLocation(deliveryJourney.getDeliveryGuy()).mapEntityType(),
                    deliveryJourney.getPathLengthRestaurantClient());
        }

        return isMoreOptimal;
    }
}