package ffWork.Payment;

import ffWork.Money.Money;

public class CardPayment extends Payment {
    private final String last4;

    public CardPayment(Money amount, String paymentId, String last4) {
        super(amount, paymentId);
        this.last4 = last4;
    }

    @Override
    public void capture() {
        if (getStatus().equals(PaymentStatus.CAPTURED)) {
            throw new IllegalStateException("Payment is already captured");
        }
        setStatus(PaymentStatus.CAPTURED);
    }

    @Override
    public String toString() {
        return "Card Payment amount: " + getAmount() + ", last4: " + last4 + ", status: " + getStatus();
    }
}
