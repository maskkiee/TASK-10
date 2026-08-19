package ffWork.CLI;

import ffWork.Domain.*;
import ffWork.Payment.Invoice;
import ffWork.Payment.Payment;
import ffWork.Pricing.HappyHoursPricing;
import ffWork.Pricing.StandardPricing;
import ffWork.Repo.InMemoryBookingRepository;
import ffWork.Repo.InMemoryResourceRepository;
import ffWork.Repo.InMemoryUserRepository;
import ffWork.Service.BillingService;
import ffWork.Service.BookingService;
import ffWork.Service.PaymentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final Scanner scanner = new Scanner(System.in);
    private final InMemoryBookingRepository bookRepo = new InMemoryBookingRepository();
    private final InMemoryResourceRepository resRepo = new InMemoryResourceRepository();
    private final InMemoryUserRepository userRepo = new InMemoryUserRepository();
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final BillingService billingService;

    public App() {
        this.bookingService = new BookingService(userRepo, resRepo, bookRepo, new StandardPricing());
        this.paymentService = new PaymentService(bookRepo);
        this.billingService = new BillingService(bookRepo);
    }

    public void run() {
        while (true) {
            System.out.println("Welcome to ffWork!");
            MenuOptions.printMenu();
            String input = scanner.nextLine();
            try {
                int num = Integer.parseInt(input);
                MenuOptions option = MenuOptions.fromNumber(num);
                if (option == MenuOptions.QUIT) {
                    quit();
                }
                switch (option) {
                    case ADD_USER -> addUser();
                    case ADD_RESOURCE -> addResource();
                    case LIST_USERS -> listUsers();
                    case LIST_RESOURCES -> listResources();
                    case BOOK -> book();
                    case CONFIRM -> confirm();
                    case CANCEL -> cancel();
                    case LIST_BOOKINGS -> listBookings();
                    case SET_PRICING -> setPricing();
                    case PAY -> pay();
                    case INVOICE -> invoice();
                    case HELP -> help();
                    default -> System.out.println("Invalid option");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Press ENTER to continue...");
            scanner.nextLine();
        }
    }

    public void addUser() {
        String type;
        while (true) {
            System.out.print("Individual user / Company user? ");
            type = scanner.nextLine().trim().toLowerCase();
            if (type.equals("company") || type.equals("individual")) {
                break;
            }
            System.out.println("Invalid type. Enter 'company' or 'individual'.");
        }
        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();
        System.out.print("Display name: ");
        String displayName = scanner.nextLine().trim();
        if (type.equals("individual")) {
            System.out.print("Student ID (or empty): ");
            String sID = scanner.nextLine().trim();
            if (sID.isEmpty()) {
                userRepo.add(new IndividualUser(email, displayName));
            } else {
                userRepo.add(new IndividualUser(email, displayName, Integer.parseInt(sID)));
            }
        }
        if (type.equals("company")) {
            System.out.print("Company name: ");
            String companyName = scanner.nextLine().trim();
            System.out.print("Tax ID: ");
            String taxID = scanner.nextLine().trim();
            userRepo.add(new CompanyUser(email, displayName, companyName, taxID));
        }
        System.out.println("USER ADDED");
    }

    private void addResource() {
        System.out.print("Type (ROOM / DESK / DEVICE): ");
        String type = scanner.nextLine().trim().toUpperCase();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Hourly rate: ");
        double rate = Double.parseDouble(scanner.nextLine().trim());
        switch (type) {
            case "ROOM" -> {
                System.out.print("Seats: ");
                int seats = Integer.parseInt(scanner.nextLine().trim());
                resRepo.add(new Room(name, seats, rate));
            }
            case "DESK" -> {
                System.out.print("Type (HOT / FIXED): ");
                Desk.DeskType dt = Desk.DeskType.valueOf(scanner.nextLine().trim().toUpperCase());
                resRepo.add(new Desk(name, dt, rate));
            }
            case "DEVICE" -> {
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());
                resRepo.add(new Device(name, qty, rate));
            }
            default -> System.out.println("ERROR: Unknown type.");
        }
        System.out.println("OK: Resource added.");
    }

    private void listUsers() {
        if (userRepo.findAll().isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("All users: ");
            userRepo.findAll().forEach(System.out::println);
        }
    }

    private void listResources() {
        if (resRepo.findAll().isEmpty()) {
            System.out.println("No resources found.");
        } else {
            System.out.println("All resources: ");
            resRepo.findAll().forEach(System.out::println);
        }
    }

    private void book() {
        System.out.println("User e-mail: ");
        String email = scanner.nextLine().trim();
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("ERROR: User not found.");
            return;
        }
        System.out.print("Resource name: ");
        String resName = scanner.nextLine().trim();
        Resource resource = resRepo.findByName(resName).orElse(null);
        if (resource == null) {
            System.out.println("ERROR: Resource not found.");
            return;
        }
        System.out.print("Start (yyyy-MM-dd'T'HH:mm): ");
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine().trim(), FMT);

        System.out.print("End time or duration? (TIME / MINUTES): ");
        String type = scanner.nextLine().trim().toUpperCase();
        LocalDateTime end;
        if (type.equals("MINUTES")) {
            System.out.print("Duration in minutes: ");
            int minutes = Integer.parseInt(scanner.nextLine().trim());
            end = start.plusMinutes(minutes);
        } else {
            System.out.print("End (yyyy-MM-dd'T'HH:mm): ");
            end = LocalDateTime.parse(scanner.nextLine().trim(), FMT);
        }

        Booking b = bookingService.book(user, resource, start, end);
        System.out.println("OK: Booking " + b.getId() + " created. Price: " + b.getCalculatedPrice());
    }

    private void confirm() {
        System.out.print("Booking ID: ");
        String id = scanner.nextLine().trim();
        bookingService.confirm(id);
        System.out.println("Booking confirmed.");
    }

    private void cancel() {
        System.out.print("Booking ID: ");
        String id = scanner.nextLine().trim();
        bookingService.cancel(id);
        System.out.println("Booking canceled.");
    }

    private void listBookings() {
        if (bookRepo.findAll().isEmpty()) {
            System.out.println("No booking found.");
        } else {
            System.out.println("All bookings: ");
            bookRepo.findAll().forEach(b -> System.out.println(b.getId() + " status: " + b.getStatus() +
                    " resource: " + b.getResource().getName() + " from: " + b.getStart() + " to: " + b.getEnd()));
        }
    }

    private void setPricing() {
        System.out.print("Policy (STANDARD / HAPPY_HOURS): ");
        String choice = scanner.nextLine().trim().toUpperCase();
        switch (choice) {
            case "STANDARD" -> bookingService.setPricingPolicy(new StandardPricing());
            case "HAPPY_HOURS" -> bookingService.setPricingPolicy(new HappyHoursPricing());
            default -> {
                System.out.println("ERROR: Unknown policy.");
            }
        }
        System.out.println("OK: Pricing set to " + choice);
    }

    private void pay() {
        System.out.print("Booking ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Card last 4 digits: ");
        String last = scanner.nextLine().trim();
        Payment pay = paymentService.pay(id, last);
        System.out.println("Payment captured. " + pay);
    }

    private void invoice() {
        System.out.print("Booking ID: ");
        String id = scanner.nextLine().trim();
        Booking b = bookingService.findOrThrow(id);
        Invoice inv = billingService.toInvoice(b);
        System.out.println("Invoice: " + inv);
    }

    private void help() {
        System.out.println("--- ffWork Menu ---");
        System.out.println(" 1. ADD USER");
        System.out.println(" 2. ADD RESOURCE");
        System.out.println(" 3. LIST USERS");
        System.out.println(" 4. LIST RESOURCE");
        System.out.println(" 5. CREATE BOOKING");
        System.out.println(" 6. CONFIRM BOOKING");
        System.out.println(" 7. CANCEL BOOKING");
        System.out.println(" 8. LIST BOOKINGS");
        System.out.println(" 9. SET PRICING POLICY");
        System.out.println("10. PAY FOR BOOKING");
        System.out.println("11. GENERATE INVOICE");
        System.out.println(" 0. EXIT");
    }

    private void quit() {
        System.out.println("Quitting the app...");
        System.exit(0);
    }
}
