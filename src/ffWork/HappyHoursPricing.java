package ffWork;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HappyHoursPricing implements PricingPolicy {

    private static final int happyHoursStart = 14;
    private static final int happyHoursEnd = 16;
    private static final double discount = 0.3;


    @Override
    public Money price(Booking booking) {
        Money hourly = booking.getResource().hourlyRate();
        Money pricePerMinute = hourly.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
        Money base = pricePerMinute.multiply(BigDecimal.valueOf(booking.durationMinutes()));
        int startHour = booking.getStart().getHour();
        if (startHour >= happyHoursStart && startHour < happyHoursEnd) {
            return base.multiply(BigDecimal.valueOf(1 - discount));
        }
        return base;
    }
}