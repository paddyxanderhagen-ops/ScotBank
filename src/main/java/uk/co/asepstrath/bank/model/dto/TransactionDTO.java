package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Pagination;
import uk.co.asepstrath.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDTO {
    private List<Transaction> transactions;
    private Pagination pagination;

    public TransactionDTO () {
        transactions = new ArrayList<>();
    }

    public List<Transaction> getTransactions () {
        return this.transactions;
    }

    public Pagination getPages () {
        return this.pagination;
    }

    public void addTransaction (Transaction transaction) {
        this.transactions.add(transaction);
    }
}

