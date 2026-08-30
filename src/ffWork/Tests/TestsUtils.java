package ffWork.Tests;

import ffWork.Domain.*;
import ffWork.Money.Money;
import ffWork.Repo.InMemoryBookingRepository;
import ffWork.Repo.InMemoryResourceRepository;
import ffWork.Repo.InMemoryUserRepository;

public final class TestsUtils {
    static InMemoryUserRepository userRepository;
    static InMemoryResourceRepository resourceRepository;
    static InMemoryBookingRepository bookingRepository;
    static Room alfa;
    static Desk hot;
    static Device projector;
    static IndividualUser annaNowak;
    static CompanyUser acme;

    static void setUp() {
        userRepository = new InMemoryUserRepository();
        resourceRepository = new InMemoryResourceRepository();
        bookingRepository = new InMemoryBookingRepository();
        alfa = new Room("Sala Alfa", 12, 80);
        resourceRepository.add(alfa);
        hot = new Desk("Hot-1", DeskType.HOT, 25);
        resourceRepository.add(hot);
        projector = new Device("Projector-1", Money.of(40), 2);
        resourceRepository.add(projector);
        annaNowak = new IndividualUser("anna@ex.pl", "Anna Nowak", 1);
        userRepository.add(annaNowak);
        acme = new CompanyUser("biuro@acme.pl", "ACME Sp. z o.o.", "ACME Sp. z o.o.", "5211234567");
        userRepository.add(acme);
    }

    static void check(String name, boolean condition) {
        if (condition) {
            System.out.println(name + " true");
        } else {
            System.out.println(name + " fail");
        }
    }
}