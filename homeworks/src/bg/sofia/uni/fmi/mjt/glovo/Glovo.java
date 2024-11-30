package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import bg.sofia.uni.fmi.mjt.glovo.exception.UnreachableClientException;

import java.util.Objects;

public class Glovo implements GlovoApi {

    private final ControlCenter controlCenter;

    public Glovo(char[][] mapLayout) {
        controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {

        if (Objects.isNull(foodItem)) {
            throw new InvalidOrderException("Null foodItem.");
        }

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
            client.location(), -1, -1, ShippingMethod.CHEAPEST);
        if (Objects.isNull(deliveryInfo.deliveryGuyLocation())) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy was found for this order.");
        }

        return new Delivery(client.location(), restaurant.location(), deliveryInfo.deliveryGuyLocation(),
            foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {

        if (Objects.isNull(foodItem)) {
            throw new IllegalArgumentException("Null foodItem.");
        }

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
            client.location(), -1, -1, ShippingMethod.FASTEST);

        if (Objects.isNull(deliveryInfo.deliveryGuyLocation())) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy was found for this order.");
        }

        return new Delivery(client.location(), restaurant.location(), deliveryInfo.deliveryGuyLocation(),
            foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant,
                                                 String foodItem, double maxPrice)
        throws NoAvailableDeliveryGuyException {

        if (Objects.isNull(foodItem)) {
            throw new IllegalArgumentException("Null foodItem.");
        }

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
            client.location(), maxPrice, -1, ShippingMethod.FASTEST);

        if (Objects.isNull(deliveryInfo.deliveryGuyLocation())) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy was found for this order.");
        }

        return new Delivery(client.location(), restaurant.location(), deliveryInfo.deliveryGuyLocation(),
            foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant,
                                                       String foodItem, int maxTime)
        throws NoAvailableDeliveryGuyException {
        if (Objects.isNull(foodItem)) {
            throw new IllegalArgumentException("Null foodItem.");
        }

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
            client.location(), -1, maxTime, ShippingMethod.CHEAPEST);

        if (Objects.isNull(deliveryInfo.deliveryGuyLocation())) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy was found for this order.");
        }

        return new Delivery(client.location(), restaurant.location(), deliveryInfo.deliveryGuyLocation(),
            foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
    }
}
