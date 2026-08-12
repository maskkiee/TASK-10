package ffWork;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void add(Booking b);
    Optional<Booking> findById(String id);
    List<Booking> findAll();
}
