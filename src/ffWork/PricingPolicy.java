package ffWork;

public interface PricingPolicy {
    Money price(Booking booking);
}
