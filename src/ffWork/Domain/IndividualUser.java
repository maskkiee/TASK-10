package ffWork.Domain;

public class IndividualUser extends User {
    private final int studentId;

    public IndividualUser(String email, String displayName, int studentId) {
        super(email, displayName);
        this.studentId = studentId;
    }

    public IndividualUser(String email, String displayName) {
        this(email, displayName, 0);
    }

    public int getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return super.toString() + " [student: " + studentId + "]";
    }
}
