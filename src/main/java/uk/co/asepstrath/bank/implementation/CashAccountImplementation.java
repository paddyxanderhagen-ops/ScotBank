package uk.co.asepstrath.bank.implementation;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class CashAccountImplementation implements uk.co.scotbank.CashAccount {
    private Investor investor;
    private static final int SCALE = 2;
    private InvestorRepository repo;

    public CashAccountImplementation () {
        this.investor = new Investor();
        this.repo = null;
    }

    public CashAccountImplementation(DataSource ds) {
        this.investor = new Investor();
        this.repo = new InvestorRepository(ds);
    }

    public CashAccountImplementation(Investor investor, DataSource ds) {
        this.investor = investor;
        this.repo = new InvestorRepository(ds);
    }

    @Override
    public double getBalance() {
        BigDecimal balance = BigDecimal.valueOf(investor.getCashBalance());
        return balance.doubleValue();
    }

    @Override
    public void deposit(double value) {
        BigDecimal amount = new BigDecimal(String.valueOf(value));
        BigDecimal currentBalance = new BigDecimal(String.valueOf(getBalance()));

        checkDecimals(value);

        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            BigDecimal newBalance = currentBalance.add(amount.setScale(SCALE, RoundingMode.FLOOR));


            newBalance = newBalance.setScale(SCALE, RoundingMode.FLOOR);

            if (repo != null) {
                try {
                    repo.transaction(investor.getId(), newBalance.doubleValue());
                } catch (SQLException e) {
                    throw new StatusCodeException(StatusCode.NOT_FOUND, "Unable to create transaction");
                }
            }
            investor.setCashBalance(newBalance.doubleValue());
        } else {
            throw new IllegalArgumentException ("Deposit amount cannot be less than 0");
        }
    }



    @Override
    public void withdraw(double value) {
        BigDecimal amount = new BigDecimal(String.valueOf(value));
        BigDecimal currentBalance = new BigDecimal(String.valueOf(getBalance()));

        checkDecimals(value);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException ("Withdraw amount cannot be less than 0");
        } else if (amount.compareTo(currentBalance) > 0) {
            throw new IllegalArgumentException("Withdraw amount cannot be greater than balance");
        }

        BigDecimal newBalance = currentBalance.subtract(amount.setScale(SCALE, RoundingMode.FLOOR));

        investor.setCashBalance(newBalance.doubleValue());
        if (repo != null) {
            try {
                repo.transaction(investor.getId(), newBalance.doubleValue());
            } catch (SQLException e) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Unable to create transaction");
            }
        }
    }

    private void checkDecimals (double value) {
        String stringV = Double.toString(Math.abs(value));
        int integerPlaces = stringV.indexOf('.');
        int decimalPlaces = stringV.length() - integerPlaces - 1;

        if (decimalPlaces > 2) {
            throw new IllegalArgumentException("Cannot accept more than 2 decimal places");
        }

    }
}
