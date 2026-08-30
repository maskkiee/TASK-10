package ffWork.Tests;

import ffWork.Domain.*;
import ffWork.Money.Money;
import ffWork.Payment.CardPayment;
import ffWork.Payment.Invoice;
import ffWork.Payment.PaymentStatus;
import ffWork.Pricing.StandardPricing;
import ffWork.Repo.InMemoryBookingRepository;
import ffWork.Services.BillingService;
import java.time.LocalDateTime;

public class TestsContinued {
    static void main() {
        System.out.println("=====Tests Pricing, Payment, Invoice=====");
        testPricing();
        testPayment();
        testInvoice();
    }

    private static void testPricing() {
        System.out.println("Standard Pricing: ");
        StandardPricing sp = new StandardPricing();
        IndividualUser u = new IndividualUser("paul321@gmail.com", "Paul", 1);
        Room room = new Room("RoomAlfa", 40, 60);

        Booking b1 = new Booking("Booking1", u, room,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 11, 0));
        check("60min = 60.00", sp.price(b1).equals(Money.of("60.00")));

        Booking b2 = new Booking("Booking2", u, room,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 11, 30));
        check("90min = 90.00", sp.price(b2).equals(Money.of("90.00")));

        Booking b3 = new Booking("Booking3", u, room,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 30));
        check("30min = 30.00", sp.price(b3).equals(Money.of("30.00")));

        Room room2 = new Room("Beta", 10, 80.0);
        Booking b4 = new Booking("Booking", u, room2,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 33));
        check("33min × 80 = 44.00", sp.price(b4).equals(Money.of("44.00")));

        System.out.println();
    }

    static void testPayment() {
        System.out.println("Card Payment: ");

        CardPayment p = new CardPayment(Money.of("100.00"), "1", "1234");

        check("INITIATED initially", p.getStatus() == PaymentStatus.INITIATED);
        p.capture();
        try {
            p.capture();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    static void testInvoice() {
        InMemoryBookingRepository bookRepo = new InMemoryBookingRepository();
        BillingService bs = new BillingService(bookRepo);
        System.out.println("Invoice: ");
        User u = new User("john123@gmail.com", "John");
        Room r = new Room("Alfa", 10, 80.0);

        Booking b1 = new Booking("Booking1", u, r,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 11, 0));
        b1.setCalculatedPrice(Money.of("160.00"));
        System.out.println("Invoice on pending: ");
        try {
            bs.toInvoice(b1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Invoice not payed: ");
        b1.setStatus(BookingStatus.CONFIRMED);
        try {
            bs.toInvoice(b1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        b1.setPayment(new CardPayment(Money.of("160.00"), "1", "1234"));
        System.out.println("Invoice is valid: ");
        Invoice invoice = bs.toInvoice(b1);
        check("Invoice created", invoice != null);
        check("Invoice num starts INV-", invoice.getInvoiceNumber().startsWith("INV-"));
        System.out.println();
    }

    static void check(String name, boolean condition) {
        if (condition) {
            System.out.println(name + " true");
        } else {
            System.out.println(name + " fail");
        }
    }
}
