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
        BigDecimal balance = BigDecimal.valueOf(investor.getCashBalance());
        return balance.doubleValue();
    }

    @Override
    public void deposit(double v) {
        BigDecimal amount = new BigDecimal(String.valueOf(v));
        BigDecimal currentBalance = new BigDecimal(String.valueOf(getBalance()));

        String stringV = Double.toString(Math.abs(v));
        int integerPlaces = stringV.indexOf('.');
        int decimalPlaces = stringV.length() - integerPlaces - 1;

        if (decimalPlaces > 2) {
            throw new IllegalArgumentException("Error: Cannot accept more than 2 decimal places");
        }

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newBalance = currentBalance.add(amount.setScale(SCALE, RoundingMode.FLOOR));


            newBalance = newBalance.setScale(SCALE, RoundingMode.FLOOR);

            investor.setCashBalance(newBalance.doubleValue());
        }
    }



    @Override
    public void withdraw(double v) {
        BigDecimal amount = new BigDecimal(String.valueOf(v));
        BigDecimal currentBalance = new BigDecimal(String.valueOf(getBalance()));

        String stringV = Double.toString(Math.abs(v));
        int integerPlaces = stringV.indexOf('.');
        int decimalPlaces = stringV.length() - integerPlaces - 1;

        if (decimalPlaces > 2) {
            throw new IllegalArgumentException("Error: Cannot accept more than 2 decimal places");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println(amount);
            throw new IllegalArgumentException ("Error: Withdraw amount cannot be less than 0");
        } else if (amount.compareTo(currentBalance) > 0) {
            throw new IllegalArgumentException("Error: Withdraw amount cannot be greater than balance");
        }

        BigDecimal newBalance = currentBalance.subtract(amount.setScale(SCALE, RoundingMode.FLOOR));

        investor.setCashBalance(newBalance.doubleValue());
    }
}
