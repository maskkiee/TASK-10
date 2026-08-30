package ffWork.Tests;

import ffWork.Domain.Booking;
import ffWork.Pricing.HappyHoursPricing;
import ffWork.Pricing.StandardPricing;
import ffWork.Services.BookingService;
import java.time.LocalDateTime;
import static ffWork.Tests.TestsUtils.*;

public class TestsExercisesContinued {
    static void main() {
        System.out.println("=====Tests Exercises 3-4=====");
        test3();
        test4();
    }

    private static void test3() {
        setUp();
        System.out.println("Test Happy Hours Pricing");
        BookingService bookingService = new BookingService(userRepository, resourceRepository, bookingRepository, new HappyHoursPricing());

        Booking happyHours = bookingService.book(annaNowak, hot,
                LocalDateTime.of(2025, 9, 17, 14, 0),
                LocalDateTime.of(2025, 9, 17, 16, 0));
        System.out.println("Happy Hours price: " + happyHours.getCalculatedPrice());
        System.out.println();
    }

    private static void test4() {
        setUp();
        System.out.println("Test overbooking");
        BookingService bookingService = new BookingService(userRepository, resourceRepository, bookingRepository, new StandardPricing());

        Booking book5 = bookingService.book(acme, projector,
                LocalDateTime.of(2025, 9, 15, 11, 0),
                LocalDateTime.of(2025, 9, 15, 13, 0));
        Booking book6 = bookingService.book(acme, projector,
                LocalDateTime.of(2025, 9, 15, 10, 0),
                LocalDateTime.of(2025, 9, 15, 12, 0));
        try {
            Booking book7 = bookingService.book(acme, projector,
                    LocalDateTime.of(2025, 9, 15, 11, 0),
                    LocalDateTime.of(2025, 9, 15, 14, 0));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }
}