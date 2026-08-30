package ffWork.Payment;

import ffWork.Domain.User;
import ffWork.Money.Money;
import java.time.LocalDateTime;

public class Invoice {
    private final String invoiceNumber;
    private final LocalDateTime issueDate;
    private final User buyer;
    private final Money total;

    public Invoice(String invoiceNumber, LocalDateTime issueDate, User buyer, Money total, String itemDescription) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.buyer = buyer;
        this.total = total;
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Money getTotal() {
        return total;
    }

    public User getBuyer() {
        return buyer;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    private final String itemDescription;

    @Override
    public String toString() {
        return "Invoice " + invoiceNumber + " [" + issueDate + "], buyer: " + buyer + " total: " + total + " desc: " + itemDescription;
    }

}
