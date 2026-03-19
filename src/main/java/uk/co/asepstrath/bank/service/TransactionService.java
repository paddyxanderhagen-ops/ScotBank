package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private TransactionRepository repo;

    public TransactionService (TransactionRepository repo) {
        this.repo = repo;
    }

    public List<Transaction> getTransactionsByInvestor (String id) throws SQLException {
        return repo.getTransactionsByInvestor(id);
    }
}
