package ffWork;

import java.math.BigDecimal;

public class Device extends Resource {
    private final int quantity;

    public int getQuantity() {
        return quantity;
    }

    public Device(String name, Money customHourlyRate, int quantity) {
        super(name, customHourlyRate);
        this.quantity = quantity;
    }

    public Device(String name, int quantity, double hourlyRate) {
        super(name, new Money(BigDecimal.valueOf(hourlyRate)));
        this.quantity = quantity;
    }

    @Override
    protected Money baseRatePerHour() {
        return new Money(BigDecimal.valueOf(50));
    }

    @Override
    public String describe() {
        return "Room " + getName() + ", Quantity: " + getQuantity();
    }
}