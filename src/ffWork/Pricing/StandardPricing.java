package ffWork.Pricing;

import ffWork.Domain.Booking;
import ffWork.Money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardPricing implements PricingPolicy {


    @Override
    public Money price(Booking booking) {
        BigDecimal hourly = booking.getResource().hourlyRate().getAmount();
        BigDecimal pricePerMinute = hourly.divide(new BigDecimal("60"), 10, RoundingMode.HALF_UP);
        BigDecimal total = pricePerMinute.multiply(BigDecimal.valueOf(booking.durationMinutes()));
        return new Money(total);
    }
}
