package ffWork.Domain;

import java.util.regex.Pattern;

public class User {
    private final String email;
    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    public User(String email, String displayName) {
        validateEmail(email);
        this.email = email;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "email= " + email + ", displayName= " + displayName;
    }
}
