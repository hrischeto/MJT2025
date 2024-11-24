package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
    JUNIOR(10),
    EXPERIENCED(0),
    SENIOR(15);

    final int fee;

    AgeGroup(int fee)
    {
        this.fee=fee;
    }
}