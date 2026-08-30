package ffWork.Payment;

import ffWork.Money.Money;

public abstract class Payment {
    private final Money amount;
    private final String paymentId;
    private PaymentStatus status;

    protected Payment(Money amount, String paymentId) {
        this.amount = amount;
        this.paymentId = paymentId;
        this.status = PaymentStatus.INITIATED;
    }

    public Money getAmount() {
        return amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public abstract void capture();
}
