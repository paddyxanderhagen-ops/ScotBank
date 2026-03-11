package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Transaction;
import java.util.List;

public class TransactionDTO {
    private List<Transaction> transactions;

    public List<Transaction> getTransactions () {
        return this.transactions;
    }
}

