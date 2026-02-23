package uk.co.asepstrath.bank.implementation;

import uk.co.asepstrath.bank.model.Investor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashAccount implements uk.co.scotbank.CashAccount {
    private final Investor investor;
    private static final int SCALE = 2;

    public CashAccount () {
        this.investor = new Investor();
    }

    public CashAccount (Investor investor) {
        this.investor = investor;
    }

    @Override
    public double getBalance() {
        return investor.getCashBalance();
    }

    @Override
    public void deposit(double amount) {
        double balance = investor.getCashBalance();

        BigDecimal bigDecimalBal = BigDecimal.valueOf(balance);
        BigDecimal bigDecimalAmount = BigDecimal.valueOf(amount);
        bigDecimalAmount = bigDecimalAmount.setScale(SCALE, RoundingMode.FLOOR);

        if (bigDecimalAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newBalance = bigDecimalBal.add(bigDecimalAmount);
            investor.setCashBalance(newBalance.doubleValue());
        }
    }

    @Override
    public void withdraw(double amount) {
        double balance = investor.getCashBalance();

        BigDecimal bigDecimalBal = BigDecimal.valueOf(balance);
        BigDecimal bigDecimalAmount = BigDecimal.valueOf(amount);
        bigDecimalAmount = bigDecimalAmount.setScale(SCALE, RoundingMode.FLOOR);

        if (bigDecimalAmount.compareTo(BigDecimal.ZERO) >= 0  && bigDecimalAmount.compareTo(bigDecimalBal) <= 0) {
            BigDecimal newBalance = bigDecimalBal.subtract(bigDecimalAmount);
            investor.setCashBalance(newBalance.doubleValue());
        } else if (bigDecimalAmount.compareTo(bigDecimalBal) > 0 || (bigDecimalAmount.compareTo(BigDecimal.ZERO) < 0)){
            throw new IllegalArgumentException ("Error: Cannot withdraw more than current balance");
        }
    }
}
