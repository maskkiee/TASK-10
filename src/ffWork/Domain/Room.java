package ffWork.Domain;

import ffWork.Money.Money;

import java.math.BigDecimal;
import java.util.Set;

public class Room extends Resource {
    private final int seats;
    private final Set<String> equipment;

    public int getSeats() {
        return seats;
    }

    public Set<String> getEquipment() {
        return equipment;
    }

    public Room(String name, int seats, Set<String> equipment, Money customHourlyRate) {
        super(name, customHourlyRate);
        this.seats = seats;
        this.equipment = equipment;
    }
    public Room(String name, int seats, double hourlyRate) {
        super(name, new Money(BigDecimal.valueOf(hourlyRate)));
        this.seats = seats;
        this.equipment = Set.of();
    }

    @Override
    protected Money baseRatePerHour() {
        return new Money(BigDecimal.valueOf(30));
    }

    @Override
    public String describe() {
        return "Room " + getName() + ", seats: " + seats;
    }
}
