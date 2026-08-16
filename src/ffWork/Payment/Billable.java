package ffWork.Payment;

import ffWork.Domain.Booking;

public interface Billable {
    Invoice toInvoice(Booking booking);
}
