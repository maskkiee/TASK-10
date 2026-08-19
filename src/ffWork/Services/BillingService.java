package ffWork.Service;

import ffWork.Domain.Booking;
import ffWork.Domain.BookingStatus;
import ffWork.Payment.Billable;
import ffWork.Payment.Invoice;
import ffWork.Repo.BookingRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillingService implements Billable {
    private final BookingRepository bookingRepo;
    private int counter = 0;

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BillingService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Invoice toInvoice(Booking booking) {
        if (booking.getStatus().equals(BookingStatus.PENDING) || booking.getStatus().equals(BookingStatus.CANCELLED)) {
            throw new IllegalArgumentException("Booking Status cannot be PENDING or CANCELLED");
        }
        if (booking.getPayment() == null) {
            throw new IllegalArgumentException("Must be paid before invoicing");
        }
        String invNumber = "INV-" + booking.getStart().format(timeFormat) + "-" + counter++;
        String desc = "Reservation: " + booking.getResource().getName() + " " + booking.getStart() + " – " + booking.getEnd();
        return new Invoice(invNumber, booking.getStart(), booking.getUser(), booking.getCalculatedPrice(), desc);
    }
}