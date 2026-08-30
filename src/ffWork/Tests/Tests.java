package ffWork.Tests;

import ffWork.Domain.*;
import ffWork.Money.Money;
import ffWork.Repo.InMemoryBookingRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Set;

public class Tests {
    static void main() {
        System.out.println("=====Tests Money, Booking, Users=====");
        testMoney();
        testUser();
        testBooking();
        testCollide();
    }

    private static void testMoney() {
        System.out.println("Money:");
        System.out.println("Adding negative money");
        try {
            Money money = new Money(BigDecimal.valueOf(-50));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Adding null in of");

        try {
            Money.of(null);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Adding negative in of");
        try {
            Money.of(-5);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Math operations (30,20)");
        Money money1 = new Money(BigDecimal.valueOf(30));
        Money money2 = new Money(BigDecimal.valueOf(20));
        Money sum = money1.add(money2);
        System.out.println("sum: " + sum);
        Money sub = money1.subtract(money2);
        System.out.println("sub: " + sub);
        Money multiply = money1.multiply(money2.getAmount());
        System.out.println("multiply: " + multiply);
        Money div = money1.divide(money2.getAmount(), 2, RoundingMode.CEILING);
        System.out.println("div: " + div);
        System.out.println();
    }

    private static void testUser() {
        System.out.println("Adding Users and Resources");
        User john = new User("john123@gmail.com", "John");
        System.out.println("John: " + john);
        IndividualUser paul = new IndividualUser("paul321@gmail.com", "Paul", 1);
        System.out.println("Paul: " + paul);
        CompanyUser jake = new CompanyUser("jake213@gmail.com", "Jake", "Jake", "01");
        System.out.println("Jake: " + jake);
        Room customRate = new Room("RoomCustomRate", 50, Set.of(), Money.of(120));
        System.out.println("Room CustomRate: " + customRate.getCustomHourlyRate());
        Room baseRate = new Room("RoomBaseRate", 40, 30);
        System.out.println("Room BaseRate: " + baseRate.getCustomHourlyRate());
        Desk deskCustomRate = new Desk("DeskCustomRate", Money.of(120), DeskType.FIXED);
        System.out.println("Desk CustomRate: " + deskCustomRate.getCustomHourlyRate());
        Desk deskBaseRate = new Desk("DeskBaseRate", DeskType.HOT, 30);
        System.out.println("Desk BaseRate: " + deskBaseRate.getCustomHourlyRate());
        Device devCustomRate = new Device("Dev Custom Rate", Money.of(50), 20);
        System.out.println("Dev CustomRate: " + devCustomRate.getCustomHourlyRate());
        Device devBaseRate = new Device("Dev Base Rate", 20, 30);
        System.out.println("Dev BaseRate: " + devBaseRate.getCustomHourlyRate());
        System.out.println();
    }

    private static void testBooking() {
        System.out.println("BookingStatus: ");

        System.out.println("PENDING→CONFIRMED? " + BookingStatus.PENDING.canTransitionTo(BookingStatus.CONFIRMED));
        System.out.println("PENDING→CANCELLED? " + BookingStatus.PENDING.canTransitionTo(BookingStatus.CANCELLED));
        System.out.println("PENDING→COMPLETED NO? " + !BookingStatus.PENDING.canTransitionTo(BookingStatus.COMPLETED));

        System.out.println("CONFIRMED→COMPLETED? " + BookingStatus.CONFIRMED.canTransitionTo(BookingStatus.COMPLETED));
        System.out.println("CONFIRMED→CANCELLED? " + BookingStatus.CONFIRMED.canTransitionTo(BookingStatus.CANCELLED));
        System.out.println("CONFIRMED→PENDING NO? " + !BookingStatus.CONFIRMED.canTransitionTo(BookingStatus.PENDING));

        System.out.println("CANCELLED→PENDING NO? " + !BookingStatus.CANCELLED.canTransitionTo(BookingStatus.PENDING));
        System.out.println("CANCELLED→CONFIRMED NO? " + !BookingStatus.CANCELLED.canTransitionTo(BookingStatus.CONFIRMED));

        System.out.println("COMPLETED→PENDING NO? " + !BookingStatus.COMPLETED.canTransitionTo(BookingStatus.PENDING));

        System.out.println();
        System.out.println("Booking: ");
        User john = new User("john123@gmail.com", "John");
        Room alfa = new Room("RoomAlfa", 40, 30);
        Booking booking = new Booking("1", john, alfa,
                LocalDateTime.of(2026, 9, 20, 15, 00),
                LocalDateTime.of(2026, 9, 25, 12, 00));
        try {
            booking.setStatus(BookingStatus.COMPLETED);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Pending→Confirmed→Completed");
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setStatus(BookingStatus.COMPLETED);
        check("Pending→Confirmed→Completed?", booking.getStatus() == BookingStatus.COMPLETED);

        System.out.println("Status to cancelled");
        try {
            booking.setStatus(BookingStatus.CANCELLED);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("End before start");
        try {
            Booking bookingEnd = new Booking("1", john, alfa,
                    LocalDateTime.of(2026, 9, 20, 15, 00),
                    LocalDateTime.of(2026, 9, 19, 12, 00));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    static void testCollide() {
        System.out.println("Booking Repository: ");

        InMemoryBookingRepository bookingRepository = new InMemoryBookingRepository();

        User u = new User("john123@gmail.com", "John");
        Room r = new Room("Alfa", 10, 80.0);

        Booking b1 = new Booking("Booking1", u, r,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 11, 0));
        Booking b2 = new Booking("Booking2", u, r,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 11, 0));
        bookingRepository.add(b1);
        bookingRepository.add(b2);

        check("findAll size ", bookingRepository.findAll().size() == 2);
        check("findById ", bookingRepository.findById("Booking1").isPresent());
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