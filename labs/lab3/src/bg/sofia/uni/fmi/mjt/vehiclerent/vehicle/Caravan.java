package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Caravan extends Vehicle{

    public Caravan(String id, String model, double pricePerDay, double pricePerHour)
    {
        super(id, model);

    }

    @Override
    protected boolean isRentalPeriodValid(LocalDateTime startRentTime, LocalDateTime rentalEnd)
    {
        return Objects.nonNull(startRentTime) && Objects.nonNull(rentalEnd) && rentalEnd.compareTo(startRentTime)<0 && startRentTime.plusDays(7).isAfter(rentalEnd);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException
    {
        return 0.0;
    }

}
