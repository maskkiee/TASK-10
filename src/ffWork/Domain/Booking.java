package ffWork.Domain;

import ffWork.Money.Money;
import ffWork.Payment.Payment;

import java.time.Duration;
import java.time.LocalDateTime;

public class Booking {
    private String id;
    private final User user;
    private final Resource resource;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private BookingStatus status;
    private Money calculatedPrice;
    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Money getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(Money calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Resource getResource() {
        return resource;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(BookingStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException("Cannot transition status to " + next);
        }
        this.status = next;
    }


    public Booking(String id, User user, Resource resource, LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end is before start time");
        }
        this.id = id;
        this.user = user;
        this.resource = resource;
        this.start = start;
        this.end = end;
        this.status = BookingStatus.PENDING;
    }

    public int durationMinutes() {
        return Math.toIntExact(Duration.between(start, end).toMinutes());
    }


}
