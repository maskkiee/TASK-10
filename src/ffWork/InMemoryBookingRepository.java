package ffWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Optional;

public class InMemoryBookingRepository implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public void add(Booking b) {
        bookings.add(b);
    }

    @Override
    public Optional<Booking> findById(String id) {
        return bookings.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    @Override
    public List<Booking> findAll() {
        return List.copyOf(bookings);
    }
}
