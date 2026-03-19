package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public List<Transaction> getTransactionsByInvestor(String id) {
        try {
            List<Transaction> transactions = repo.getTransactionsByInvestor(id);

            if (transactions == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Transactions not found for investor");
            }
            return transactions;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve transactions");
        }
    }
}
