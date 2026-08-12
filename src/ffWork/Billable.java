package ffWork;

public interface Billable {
    Invoice toInvoice(Booking booking);
}
