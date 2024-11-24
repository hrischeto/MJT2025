package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Bicycle extends Vehicle{

    private double pricePerDay;
    private double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour)
    {
        super(id, model);

        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;
    }

    @Override
    protected boolean isRentalPeriodValid(LocalDateTime startRentTime, LocalDateTime rentalEnd)
    {
        return Objects.nonNull(startRentTime) && Objects.nonNull(rentalEnd) && rentalEnd.compareTo(startRentTime)<0 && startRentTime.plusDays(7).isAfter(rentalEnd);
    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     * the period is not valid (end date is before start date)
     */
    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException
    {
        if(isRentalPeriodValid(startOfRent, endOfRent))
        {
            throw new InvalidRentingPeriodException("Invalid period!");
        }

        return (endOfRent.getDayOfMonth()-startOfRent.getDayOfMonth())*pricePerDay+
                endOfRent.getHour()
                ;
    }

}
