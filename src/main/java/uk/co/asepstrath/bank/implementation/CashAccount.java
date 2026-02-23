package uk.co.asepstrath.bank.implementation;

import uk.co.asepstrath.bank.model.Investor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashAccount implements uk.co.scotbank.CashAccount {
    private Investor investor;
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
    public void deposit(double v) {
        double balance = investor.getCashBalance();

        double amount = Math.floor(v * 100.0) / 100.0;

        if (v > 0) {
            double newBalance = balance + amount;
            investor.setCashBalance(newBalance);
        }
    }

    @Override
    public void withdraw(double v) {
        double balance = investor.getCashBalance();

        double amount = Math.floor(v * 100.0) / 100.0;

        if (amount >= 0  && amount <= balance) {
            double newBalance = balance - amount;
            investor.setCashBalance(newBalance);
        } else if (amount > balance){
            throw new IllegalArgumentException ("Error: Cannot withdraw more than current balance");
        }
    }
}
