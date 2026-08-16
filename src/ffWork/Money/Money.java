package ffWork.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public Money(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount cannot be null");
        }
        if (amount.contains("-")) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        return new Money(new BigDecimal(amount));
    }

    public static Money of(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(BigDecimal other) {
        return new Money(amount.multiply(other));
    }

    public Money multiply(double other) {
        return new Money(amount.multiply(BigDecimal.valueOf(other)));
    }

    public Money divide(BigDecimal divisor, int scale, RoundingMode mode) {
        return new Money(this.amount.divide(divisor, scale, mode));
    }

    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return amount.toString() + " PLN";
    }
}
