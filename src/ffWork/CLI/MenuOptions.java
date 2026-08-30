package ffWork.CLI;

public enum MenuOptions {
    ADD_USER("Add user", 1),
    ADD_RESOURCE("Add resource", 2),
    LIST_USERS("List users", 3),
    LIST_RESOURCES("List resources", 4),
    BOOK("Create booking", 5),
    CONFIRM("Confirm booking", 6),
    CANCEL("Cancel booking", 7),
    LIST_BOOKINGS("List bookings", 8),
    SET_PRICING("Set pricing policy", 9),
    PAY("Pay for booking", 10),
    INVOICE("Generate invoice", 11),
    HELP("Show help", 12),
    QUIT("Exit", 0);

    private final String description;
    private final int number;

    MenuOptions(String description, int number) {
        this.description = description;
        this.number = number;
    }

    public static MenuOptions fromNumber(int number) {
        for (MenuOptions o : values()) {
            if (o.number == number) {
                return o;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public int getNumber() {
        return number;
    }

    public static void printMenu() {
        System.out.println("===== ffWork =====");
        for (MenuOptions o : values()) {
            System.out.println(o.number + ". " + o.description);
        }
        System.out.println("Choice: ");
    }
}