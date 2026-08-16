package ffWork.Domain;

public class CompanyUser extends User {
    private final String companyName;
    private final String taxId;

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxId() {
        return taxId;
    }


    public CompanyUser(String email, String displayName, String companyName, String taxId) {
        super(email, displayName);
        this.companyName = companyName;
        this.taxId = taxId;
    }

    @Override
    public String toString() {
        return "email: " + getEmail() + " companyName: " + companyName + " [NIP: " + taxId + "]";
    }
}
