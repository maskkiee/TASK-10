package ffWork.Domain;

public class User {
    private final String email;
    private final String displayName;

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "email= " + email + ", displayName= " + displayName;
    }
}
