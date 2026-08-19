package ffWork.Tests;

import ffWork.Domain.*;
import ffWork.Money.Money;
import ffWork.Payment.CardPayment;
import ffWork.Payment.Invoice;
import ffWork.Payment.Payment;
import ffWork.Payment.PaymentStatus;
import ffWork.Pricing.HappyHoursPricing;
import ffWork.Pricing.StandardPricing;
import ffWork.Repo.InMemoryBookingRepository;
import ffWork.Repo.InMemoryResourceRepository;
import ffWork.Repo.InMemoryUserRepository;
import ffWork.Service.BillingService;
import ffWork.Service.BookingService;
import ffWork.Service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestsExercises {
    static void main() {
        InMemoryUserRepository userRepo = new InMemoryUserRepository();
        InMemoryResourceRepository resRepo = new InMemoryResourceRepository();
        InMemoryBookingRepository bookRepo = new InMemoryBookingRepository();
        BookingService bookingService = new BookingService(userRepo, resRepo, bookRepo, new StandardPricing());
        PaymentService paymentService = new PaymentService(bookRepo);
        BillingService billingService = new BillingService(bookRepo);

        System.out.println("Dane Testowe:");
        Room alfa = new Room("Sala Alfa", 12, 80);
        resRepo.add(alfa);
        Desk hot = new Desk("Hot-1", Desk.DeskType.HOT, 25);
        resRepo.add(hot);
        Device projector = new Device("Projector-1", Money.of(40), 2);
        resRepo.add(projector);
        IndividualUser annaNowak = new IndividualUser("anna@ex.pl", "Anna Nowak", 1);
        userRepo.add(annaNowak);
        CompanyUser ACME = new CompanyUser("biuro@acme.pl", "ACME Sp. z o.o.", "ACME Sp. z o.o.", "5211234567");
        userRepo.add(ACME);

        System.out.println("=====Test 1=====");
        Booking b1 = bookingService.book(ACME, alfa,
                LocalDateTime.of(2025, 9, 15, 10, 0),
                LocalDateTime.of(2025, 9, 15, 12, 0));
        System.out.println("Booking Status: " + b1.getStatus() + " Price: " + b1.getCalculatedPrice());
        bookingService.confirm(b1.getId());
        check("B1 status confirmed? ", b1.getStatus() == BookingStatus.CONFIRMED);
        Payment pay = paymentService.pay(b1.getId(), "4242");
        check("Payment Status: " + pay.getStatus(), pay.getStatus() == PaymentStatus.CAPTURED);
        Invoice invoice = billingService.toInvoice(b1);
        System.out.println(invoice);
        Booking b2 = bookingService.book(ACME, alfa,
                LocalDateTime.of(2025, 9, 16, 9, 0), 90);
        System.out.println("Booking Status: " + b2.getStatus() + " Price: " + b2.getCalculatedPrice());
        System.out.println();
        System.out.println("=====Test 2=====");
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
        System.out.println("=====Test 3=====");
        BookingService bookingServiceHH = new BookingService(userRepo, resRepo, bookRepo, new HappyHoursPricing());
        Booking bhh = bookingServiceHH.book(annaNowak, hot, LocalDateTime.of(2025, 9, 17, 14, 0),
                LocalDateTime.of(2025, 9, 17, 16, 0));
        System.out.println("bhh price: " + bhh.getCalculatedPrice());
        System.out.println();
        System.out.println("=====Test 4=====");
        Booking book5 = bookingService.book(ACME, projector,
                LocalDateTime.of(2025, 9, 15, 11, 0),
                LocalDateTime.of(2025, 9, 15, 13, 0));
        Booking book6 = bookingService.book(ACME, projector,
                LocalDateTime.of(2025, 9, 15, 10, 0),
                LocalDateTime.of(2025, 9, 15, 12, 0));
        try {
            Booking book7 = bookingService.book(ACME, projector,
                    LocalDateTime.of(2025, 9, 15, 11, 0),
                    LocalDateTime.of(2025, 9, 15, 14, 0));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    static void check(String name, boolean condition) {
        if (condition) {
            System.out.println(name + " true");
        } else {
            System.out.println(name + " fail");
        }
    }
}