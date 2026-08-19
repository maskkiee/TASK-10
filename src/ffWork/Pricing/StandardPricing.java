package ffWork.Pricing;

import ffWork.Domain.Booking;
import ffWork.Money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardPricing implements PricingPolicy {


    @Override
    public Money price(Booking booking) {
        Money hourly = booking.getResource().hourlyRate();
        Money pricePerMinute = hourly.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
        return pricePerMinute.multiply(BigDecimal.valueOf(booking.durationMinutes()));
    }
}
