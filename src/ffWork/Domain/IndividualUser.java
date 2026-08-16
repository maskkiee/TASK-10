package ffWork.Domain;

public class IndividualUser extends User {
    private final int studentId;

    public int getStudentId() {
        return studentId;
    }

    public IndividualUser(String email, String displayName, int studentId) {
        super(email, displayName);
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return super.toString() + " [student: " + studentId + "]";
    }
}
