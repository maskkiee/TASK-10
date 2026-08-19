package ffWork.Domain;

public class User {
    private final String email;
    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "email= " + email + ", displayName= " + displayName;
    }
}
