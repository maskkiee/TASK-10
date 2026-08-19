package ffWork.Pricing;

import ffWork.Domain.Booking;
import ffWork.Money.Money;

public interface PricingPolicy {
    Money price(Booking booking);
}
