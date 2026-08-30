package ffWork.Services;

import ffWork.Domain.*;
import ffWork.Pricing.PricingPolicy;
import ffWork.Repo.BookingRepository;
import ffWork.Repo.ResourceRepository;
import ffWork.Repo.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingService {
    private final UserRepository userRepo;
    private final ResourceRepository resourceRepo;
    private final BookingRepository bookingRepo;
    private PricingPolicy pricingPolicy;
    private int counter = 0;

    public PricingPolicy getPricingPolicy() {
        return pricingPolicy;
    }

    public void setPricingPolicy(PricingPolicy pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
    }

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BookingService(UserRepository userRepo, ResourceRepository resourceRepo, BookingRepository bookingRepo, PricingPolicy pricingPolicy) {
        this.userRepo = userRepo;
        this.resourceRepo = resourceRepo;
        this.bookingRepo = bookingRepo;
        this.pricingPolicy = pricingPolicy;
    }

    public void checkAvailability(Resource resource, LocalDateTime start, LocalDateTime end) {
        if (resource instanceof Device device) {
            long overlapping = bookingRepo.findAll().stream()
                    .filter(b -> b.getResource().equals(device))
                    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING)
                    .filter(b -> b.getStart().isBefore(end) && b.getEnd().isAfter(start))
                    .count();
            if (overlapping >= device.getQuantity()) {
                throw new IllegalArgumentException("Resource not available. Too many units booked.");
            }
        } else {
            boolean conflict = bookingRepo.findAll().stream()
                    .filter(b -> b.getResource().equals(resource))
                    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING)
                    .anyMatch(b -> b.getStart().isBefore(end) && b.getEnd().isAfter(start));
            if (conflict) {
                throw new IllegalArgumentException("Resource not available. Booking conflict.");
            }
        }
    }

    public List<Booking> list() {
        return bookingRepo.findAll();
    }

    public Booking findOrThrow(String id) {
        return bookingRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
    }

    public void confirm(String bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
    }

    public void cancel(String bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public void complete(String bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.setStatus(BookingStatus.COMPLETED);
    }

    public String generateId(LocalDateTime start) {
        counter++;
        return "B-" + start.format(timeFormat) + "-" + counter;
    }

    public Booking book(User user, Resource resource, LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        checkAvailability(resource, start, end);
        String id = generateId(start);
        Booking booking = new Booking(id, user, resource, start, end);
        booking.setCalculatedPrice(pricingPolicy.price(booking));
        bookingRepo.add(booking);
        return booking;
    }

    public Booking book(User user, Resource resource, LocalDateTime start, int durationMinutes) {
        return book(user, resource, start, start.plusMinutes(durationMinutes));
    }
}
