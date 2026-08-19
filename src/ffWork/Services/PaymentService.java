package ffWork.Service;

import ffWork.Domain.Booking;
import ffWork.Domain.BookingStatus;
import ffWork.Money.Money;
import ffWork.Payment.CardPayment;
import ffWork.Payment.Payment;
import ffWork.Repo.BookingRepository;

public class PaymentService {
    private final BookingRepository bookingRepo;
    private int counter = 0;

    public PaymentService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public Payment pay(String bookingId, String cardLast4) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Booking must be confirmed");
        }
        Money amount = new Money(booking.getCalculatedPrice().getAmount());
        String paymentId = "PAY-" + counter++;
        CardPayment payment = new CardPayment(amount, paymentId, cardLast4);
        payment.capture();
        booking.setPayment(payment);
        return payment;
    }

}