package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDTO {
    private List<Transaction> transactions;

    public TransactionDTO () {
        transactions = new ArrayList<>();
    }

    public List<Transaction> getTransactions () {
        return this.transactions;
    }

    public void addTransaction (Transaction transaction) {
        this.transactions.add(transaction);
    }
}

