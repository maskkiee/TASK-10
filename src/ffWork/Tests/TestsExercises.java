package ffWork.Tests;

import ffWork.Domain.*;
import ffWork.Payment.Invoice;
import ffWork.Payment.Payment;
import ffWork.Payment.PaymentStatus;
import ffWork.Pricing.StandardPricing;
import ffWork.Services.BillingService;
import ffWork.Services.BookingService;
import ffWork.Services.PaymentService;
import java.time.LocalDateTime;

import static ffWork.Tests.TestsUtils.*;

public class TestsExercises {
    static void main() {
        System.out.println("=====Tests Exercises 1-2=====");
        test1();
        test2();
    }

    private static void test1() {
        setUp();
        System.out.println("Test booking");
        BookingService bookingService = new BookingService(userRepository, resourceRepository, bookingRepository, new StandardPricing());
        PaymentService paymentService = new PaymentService(bookingRepository);
        BillingService billingService = new BillingService(bookingRepository);

        Booking b1 = bookingService.book(acme, alfa,
                LocalDateTime.of(2025, 9, 15, 10, 0),
                LocalDateTime.of(2025, 9, 15, 12, 0));
        System.out.println("Booking Status: " + b1.getStatus() + " Price: " + b1.getCalculatedPrice());
        bookingService.confirm(b1.getId());
        check("B1 status confirmed? ", b1.getStatus() == BookingStatus.CONFIRMED);
        Payment pay = paymentService.pay(b1.getId(), "4242");
        check("Payment Status: " + pay.getStatus() + "?", pay.getStatus() == PaymentStatus.CAPTURED);
        Invoice invoice = billingService.toInvoice(b1);
        System.out.println(invoice);

        Booking b2 = bookingService.book(acme, alfa,
                LocalDateTime.of(2025, 9, 16, 9, 0), 90);
        System.out.println("Booking Status: " + b2.getStatus() + " Price: " + b2.getCalculatedPrice());
        System.out.println();
    }

    private static void test2() {
        setUp();
        System.out.println("Test collision");
        BookingService bookingService = new BookingService(userRepository, resourceRepository, bookingRepository, new StandardPricing());

        Booking b1 = bookingService.book(acme, alfa,
                LocalDateTime.of(2025, 9, 15, 10, 0),
                LocalDateTime.of(2025, 9, 15, 12, 0));
        try {
            Booking b3 = bookingService.book(annaNowak, alfa,
                    LocalDateTime.of(2025, 9, 15, 11, 0),
                    LocalDateTime.of(2025, 9, 15, 13, 0));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        Booking b4 = bookingService.book(annaNowak, hot,
                LocalDateTime.of(2025, 9, 15, 11, 0),
                LocalDateTime.of(2025, 9, 15, 13, 0));
        check("b4 status confirmed? ", b4.getStatus() == BookingStatus.CONFIRMED);
        System.out.println();
    }
}