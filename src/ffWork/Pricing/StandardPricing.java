package ffWork.Pricing;

import ffWork.Domain.Booking;
import ffWork.Money.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardPricing implements PricingPolicy {
    private static final BigDecimal HOURLY_RATE = new BigDecimal("60");

    @Override
    public Money price(Booking booking) {
        Money hourly = booking.getResource().hourlyRate();
        Money pricePerMinute = hourly.divide(HOURLY_RATE, 2, RoundingMode.HALF_UP);
        return pricePerMinute.multiply(BigDecimal.valueOf(booking.durationMinutes()));
    }
}
