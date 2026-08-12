package ffWork;

import java.math.BigDecimal;

public class Desk extends Resource {
    public enum DeskType {HOT, FIXED}

    private final DeskType type;

    public DeskType getType() {
        return type;
    }

    public Desk(String name, Money customHourlyRate, DeskType type) {
        super(name, customHourlyRate);
        this.type = type;
    }

    public Desk(String name, DeskType type, double hourlyRate) {
        super(name, new Money(BigDecimal.valueOf(hourlyRate)));
        this.type = type;
    }

    @Override
    protected Money baseRatePerHour() {
        return new Money(BigDecimal.valueOf(40));
    }

    @Override
    public String describe() {
        return "Room " + getName() + ", Desk type: " + getType();
    }
}
