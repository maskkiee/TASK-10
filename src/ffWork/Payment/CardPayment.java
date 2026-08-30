package ffWork.Payment;

import ffWork.Money.Money;

public class CardPayment extends Payment {
    private final String cardLast4;

    public CardPayment(Money amount, String paymentId, String cardLast4) {
        super(amount, paymentId);
        this.cardLast4 = cardLast4;
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
        return "Card Payment amount: " + getAmount() + ", cardLast4: " + cardLast4 + ", status: " + getStatus();
    }
}
