package uk.co.asepstrath.bank.implementation;

import uk.co.asepstrath.bank.model.Investor;

import java.math.BigDecimal;

public class CashAccount implements uk.co.scotbank.CashAccount {
    private Investor investor;

    public CashAccount () {
        this.investor = new Investor();
    }

    public CashAccount (Investor investor) {
        this.investor = investor;
    }

    @Override
    public double getBalance() {
        BigDecimal bigDecimal = investor.getCashBalance();
        double balance = bigDecimal.doubleValue();
        return balance;
    }

    @Override
    public void deposit(double amount) {
        double balance = investor.getCashBalance().doubleValue();
        if (amount >= 0.00) {
            double roundedAmount = Math.floor(amount * (double) 100.0F) / (double) 100.0F;
//            if ((amount - roundedAmount)*100 > 0.5){
//                roundedAmount = Math.ceil(amount * (double) 100.0F) / (double) 100.0F;
//            }
            double newBal = balance + roundedAmount;
            investor.setCashBalance(BigDecimal.valueOf(newBal));
        } else {
            System.out.println("Fail");
        }
    }

    @Override
    public void withdraw(double amount) throws IllegalArgumentException{
        double balance = investor.getCashBalance().doubleValue();

        if (amount >= 0.00 && amount <= balance) {
            double roundedAmount = Math.floor(amount * (double) 100.0F) / (double) 100.0F;
//            if ((amount - roundedAmount)*100 > 0.5){
//                roundedAmount = Math.ceil(amount * (double) 100.0F) / (double) 100.0F;
//            }

            double newBal = balance - roundedAmount;
            investor.setCashBalance(BigDecimal.valueOf(newBal));
        } else if (amount > balance){
            throw new IllegalArgumentException ("You cannot withdraw more than your current balance.");
        }
    }
}
