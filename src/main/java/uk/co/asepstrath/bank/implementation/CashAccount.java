package uk.co.asepstrath.bank.implementation;

import uk.co.asepstrath.bank.model.Investor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashAccount implements uk.co.scotbank.CashAccount {
    private Investor investor;
    final int ROUND = 2;

    public CashAccount (Investor investor) {
        this.investor = investor;
    }

    @Override
    public double getBalance() {
        BigDecimal bigDecimal = investor.getCashBalance().setScale(ROUND, RoundingMode.FLOOR);
        double balance = bigDecimal.doubleValue();
        return balance;
    }

    @Override
    public void deposit(double v) {
        BigDecimal roundedV = BigDecimal.valueOf(v);
        roundedV = roundedV.setScale(ROUND, RoundingMode.FLOOR);
        v = roundedV.doubleValue();

        if (v >= 0.00) {
            BigDecimal currBal = investor.getCashBalance();
            investor.setCashBalance(currBal.add(BigDecimal.valueOf(v)));
            //create new transaction
        } else {
            System.out.println("Fail");
        }
    }

    @Override
    public void withdraw(double v) {

    }
}
