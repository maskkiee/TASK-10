package ffWork.Pricing;

import ffWork.Domain.Booking;
import ffWork.Money.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class HappyHoursPricing implements PricingPolicy {
    private static final int HAPPY_HOURS_START = 14;
    private static final int HAPPY_HOURS_END = 16;
    private static final double DISCOUNT = 0.3;
    private static final BigDecimal HOURLY_RATE = new BigDecimal("60");

    @Override
    public Money price(Booking booking) {
        Money hourly = booking.getResource().hourlyRate();
        Money pricePerMinute = hourly.divide(HOURLY_RATE, 10, RoundingMode.HALF_UP);
        Money base = pricePerMinute.multiply(BigDecimal.valueOf(booking.durationMinutes()));
        int startHour = booking.getStart().getHour();
        if (startHour >= HAPPY_HOURS_START && startHour < HAPPY_HOURS_END) {
            return base.multiply(BigDecimal.valueOf(1 - DISCOUNT));
        }
        return base;
    }
}