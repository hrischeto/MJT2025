package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Vehicle {

    private final String id;
    private String model;

    private Driver driver=null;
    private LocalDateTime startRentTime=null;
    private boolean isRented=false;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
    }

    public void rent(Driver driver, LocalDateTime startRentTime) {
        if(!isRented)
        {
            this.driver=driver;
            this.startRentTime=startRentTime;
            isRented=true;
        }
        else if(Objects.equals(this.driver, driver)) {
            throw new VehicleAlreadyRentedException("You already rented vehicle " + id + "!");
        }
        else
        {
            throw new VehicleAlreadyRentedException("Vehicle "+id+" already rented!");

        }

    }

    protected abstract boolean isRentalPeriodValid(LocalDateTime startRentTime,LocalDateTime rentalEnd);

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException in case @rentalEnd is null
     * @throws VehicleNotRentedException in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     * in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     * and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
       if(Objects.isNull(rentalEnd))
       {
           throw new IllegalArgumentException("rentalEnd cannot be null");
       }
       if(!isRented)
       {
           throw new VehicleNotRentedException("Vehicle "+id+" not rented!");
       }
       if(isRentalPeriodValid(this.startRentTime, rentalEnd))
       {
           throw new InvalidRentingPeriodException("Invalid rental end time!");
       }

       isRented=false;

       driver=null;
       startRentTime=null;
    }

    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

}