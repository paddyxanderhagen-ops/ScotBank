package uk.co.asepstrath.bank;

import uk.co.asepstrath.bank.model.Investor;

import java.math.BigDecimal;

public class Account extends Investor {
    public void deposit(int amount) {
        setCashBalance(getCashBalance().add(BigDecimal.valueOf(amount)));
    }

    public BigDecimal getBalance() {
        return getCashBalance();
    }

}
